/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsadmin.web.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import se.inera.intyg.infra.intyginfo.dto.IntygInfo;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent.Source;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEventType;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.IntygInfoPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoListDTO;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.mapper.IntygInfoMapper;

@ExtendWith(MockitoExtension.class)
class IntygInfoServiceImplTest {

    @Spy
    private IntygInfoMapper intygInfoMapper = Mappers.getMapper(IntygInfoMapper.class);
    @Mock
    private IntygInfoPersistenceService intygInfoPersistenceService;
    @Mock
    private UserService userService;
    @Mock
    private ITIntegrationRestService itIntegrationRestService;
    @Mock
    private WCIntegrationRestService wcIntegrationRestService;
    @InjectMocks
    private IntygInfoServiceImpl intygInfoService;

    @Test
    void testGetIntygInfoList() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<IntygInfoEntity> persistenceResult = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(intygInfoPersistenceService.findAll(any(Pageable.class))).thenReturn(persistenceResult);

        Page<IntygInfoListDTO> intygInfoList = intygInfoService.getIntygInfoList(pageable);

        verify(intygInfoPersistenceService, times(1)).findAll(eq(pageable));
        assertEquals(0, intygInfoList.getTotalElements());
    }

    @Test
    void testGetIntygInfoNotFound() {
        String intygId = "intygId";

        when(wcIntegrationRestService.getIntygInfo(intygId)).thenReturn(null);
        when(itIntegrationRestService.getIntygInfo(intygId)).thenReturn(null);

        Optional<IntygInfoDTO> intygInfo = intygInfoService.getIntygInfo(intygId);

        assertFalse(intygInfo.isPresent());
        verifyNoInteractions(intygInfoPersistenceService);
    }

    @Test
    void testGetIntygInfoFoundInIT() {
        mockUser();

        String intygId = "intygId";

        ItIntygInfo itIntygInfo = new ItIntygInfo();
        itIntygInfo.setIntygId(intygId);
        itIntygInfo.setIntygType("lisjp");
        itIntygInfo.getEvents().add(new IntygInfoEvent(Source.INTYGSTJANSTEN, LocalDateTime.now(), IntygInfoEventType.IS005));

        when(wcIntegrationRestService.getIntygInfo(intygId)).thenReturn(null);
        when(itIntegrationRestService.getIntygInfo(intygId)).thenReturn(itIntygInfo);

        Optional<IntygInfoDTO> optionalIntygInfo = intygInfoService.getIntygInfo(intygId);

        assertTrue(optionalIntygInfo.isPresent());

        IntygInfoDTO intygInfo = optionalIntygInfo.get();
        assertEquals("lisjp", intygInfo.getIntygType());
        assertEquals(1, intygInfo.getEvents().size());
        assertEquals(Source.INTYGSTJANSTEN, intygInfo.getEvents().getFirst().getSource());
        assertFalse(intygInfo.isCreatedInWC());

        verify(intygInfoPersistenceService, times(1)).create(createIntyInfo(itIntygInfo));
    }

    @Test
    void testGetIntygInfoFoundInWC() {
        mockUser();

        String intygId = "intygId";

        WcIntygInfo wcIntygInfo = new WcIntygInfo();
        wcIntygInfo.setCreatedInWC(true);
        wcIntygInfo.setIntygId(intygId);
        wcIntygInfo.setIntygType("lisjp");
        wcIntygInfo.setDraftCreated(LocalDateTime.now());
        wcIntygInfo.getEvents().add(new IntygInfoEvent(Source.WEBCERT, LocalDateTime.now(), IntygInfoEventType.IS001));

        when(wcIntegrationRestService.getIntygInfo(intygId)).thenReturn(wcIntygInfo);
        when(itIntegrationRestService.getIntygInfo(intygId)).thenReturn(null);

        Optional<IntygInfoDTO> optionalIntygInfo = intygInfoService.getIntygInfo(intygId);

        assertTrue(optionalIntygInfo.isPresent());

        IntygInfoDTO intygInfo = optionalIntygInfo.get();
        assertEquals("lisjp", intygInfo.getIntygType());
        assertEquals(1, intygInfo.getEvents().size());
        assertEquals(Source.WEBCERT, intygInfo.getEvents().getFirst().getSource());
        assertTrue(intygInfo.isCreatedInWC());

        verify(intygInfoPersistenceService, times(1)).create(createIntyInfo(wcIntygInfo));
    }

    @Test
    void testGetIntygInfo() {
        mockUser();

        String intygId = "intygId";

        WcIntygInfo wcIntygInfo = new WcIntygInfo();
        wcIntygInfo.setCreatedInWC(true);
        wcIntygInfo.setIntygId(intygId);
        wcIntygInfo.setIntygType("lisjp");
        wcIntygInfo.setDraftCreated(LocalDateTime.now());
        wcIntygInfo.getEvents().add(new IntygInfoEvent(Source.WEBCERT, LocalDateTime.now(), IntygInfoEventType.IS001));

        ItIntygInfo itIntygInfo = new ItIntygInfo();
        itIntygInfo.setIntygId(intygId);
        itIntygInfo.setIntygType("lisjp");
        itIntygInfo.getEvents().add(new IntygInfoEvent(Source.INTYGSTJANSTEN, LocalDateTime.now(), IntygInfoEventType.IS005));

        when(wcIntegrationRestService.getIntygInfo(intygId)).thenReturn(wcIntygInfo);
        when(itIntegrationRestService.getIntygInfo(intygId)).thenReturn(itIntygInfo);

        Optional<IntygInfoDTO> optionalIntygInfo = intygInfoService.getIntygInfo(intygId);

        assertTrue(optionalIntygInfo.isPresent());

        IntygInfoDTO intygInfo = optionalIntygInfo.get();
        assertEquals("lisjp", intygInfo.getIntygType());
        assertTrue(intygInfo.isCreatedInWC());
        assertEquals(2, intygInfo.getEvents().size());
        assertEquals(Source.WEBCERT, intygInfo.getEvents().get(1).getSource());
        assertEquals(Source.INTYGSTJANSTEN, intygInfo.getEvents().get(0).getSource());

        verify(intygInfoPersistenceService, times(1)).create(createIntyInfo(wcIntygInfo));
    }

    @Test
    void testGetIntygInfoDateNull() {
        mockUser();

        String intygId = "intygId";

        WcIntygInfo wcIntygInfo = new WcIntygInfo();
        wcIntygInfo.setCreatedInWC(true);
        wcIntygInfo.setIntygId(intygId);
        wcIntygInfo.setIntygType("lisjp");
        wcIntygInfo.setDraftCreated(LocalDateTime.now());
        wcIntygInfo.getEvents().add(new IntygInfoEvent(Source.WEBCERT, null, IntygInfoEventType.IS001));

        ItIntygInfo itIntygInfo = new ItIntygInfo();
        itIntygInfo.setIntygId(intygId);
        itIntygInfo.setIntygType("lisjp");
        itIntygInfo.getEvents().add(new IntygInfoEvent(Source.INTYGSTJANSTEN, LocalDateTime.now(), IntygInfoEventType.IS005));

        when(wcIntegrationRestService.getIntygInfo(intygId)).thenReturn(wcIntygInfo);
        when(itIntegrationRestService.getIntygInfo(intygId)).thenReturn(itIntygInfo);

        Optional<IntygInfoDTO> optionalIntygInfo = intygInfoService.getIntygInfo(intygId);

        assertTrue(optionalIntygInfo.isPresent());

        IntygInfoDTO intygInfo = optionalIntygInfo.get();
        assertEquals("lisjp", intygInfo.getIntygType());
        assertTrue(intygInfo.isCreatedInWC());
        assertEquals(2, intygInfo.getEvents().size());
        assertEquals(Source.WEBCERT, intygInfo.getEvents().get(1).getSource());
        assertEquals(Source.INTYGSTJANSTEN, intygInfo.getEvents().get(0).getSource());

        verify(intygInfoPersistenceService, times(1)).create(any());
    }

    private void mockUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmployeeHsaId("hsaId");
        userEntity.setName("User1");

        final var oidcIdToken = new OidcIdToken("tokenValue", null, null, Map.of("employeeHsaId", "hsaId"));
        IntygsadminUser user = new IntygsadminUser(userEntity, AuthenticationMethod.FAKE, oidcIdToken,
            Collections.emptySet(), "employeeHsaId");
        when(userService.getActiveUser()).thenReturn(user);
    }

    private IntygInfoEntity createIntyInfo(IntygInfo intygInfo) {
        IntygInfoEntity intygInfoEntity = new IntygInfoEntity();
        intygInfoEntity.setIntygId(intygInfo.getIntygId());
        intygInfoEntity.setEmployeeHsaId(userService.getActiveUser().getEmployeeHsaId());
        intygInfoEntity.setEmployeeName(userService.getActiveUser().getName());
        intygInfoEntity.setEnhetsId(intygInfo.getCareUnitHsaId());
        intygInfoEntity.setVardgivarId(intygInfo.getCareGiverHsaId());

        return intygInfoEntity;
    }
}
