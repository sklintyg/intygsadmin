/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent.Source;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEventType;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.IntygInfoPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoListDTO;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationService;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationService;
import se.inera.intyg.intygsadmin.web.mapper.IntygInfoMapper;

@ExtendWith(MockitoExtension.class)
public class IntygInfoServiceTest {

    @Spy
    private IntygInfoMapper intygInfoMapper = Mappers.getMapper(IntygInfoMapper.class);
    @Mock
    private IntygInfoPersistenceService intygInfoPersistenceService;
    @Mock
    private UserService userService;
    @Mock
    private ITIntegrationService itIntegrationService;
    @Mock
    private WCIntegrationService wcIntegrationService;
    @InjectMocks
    private IntygInfoService intygInfoService;

    @Test
    public void testGetIntygInfoList() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<IntygInfoEntity> persistenceResult = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(intygInfoPersistenceService.findAll(any(Pageable.class))).thenReturn(persistenceResult);

        Page<IntygInfoListDTO> intygInfoList = intygInfoService.getIntygInfoList(pageable);

        verify(intygInfoPersistenceService, times(1)).findAll(eq(pageable));
        assertEquals(0, intygInfoList.getTotalElements());
    }

    @Test
    public void testGetIntygInfoNotFound() {
        String intygId = "intygId";

        when(wcIntegrationService.getIntygInfo(intygId)).thenReturn(null);
        when(itIntegrationService.getIntygInfo(intygId)).thenReturn(null);

        Optional<IntygInfoDTO> intygInfo = intygInfoService.getIntygInfo(intygId);

        assertFalse(intygInfo.isPresent());
        verifyZeroInteractions(intygInfoPersistenceService);
    }

    @Test
    public void testGetIntygInfoFoundInIT() {
        mockUser();

        String intygId = "intygId";

        ItIntygInfo itIntygInfo = new ItIntygInfo();
        itIntygInfo.setIntygId(intygId);
        itIntygInfo.setIntygType("lisjp");
        itIntygInfo.getEvents().add(new IntygInfoEvent(Source.INTYGSTJANSTEN, LocalDateTime.now(), IntygInfoEventType.IS005));

        when(wcIntegrationService.getIntygInfo(intygId)).thenReturn(null);
        when(itIntegrationService.getIntygInfo(intygId)).thenReturn(itIntygInfo);

        Optional<IntygInfoDTO> optionalIntygInfo = intygInfoService.getIntygInfo(intygId);

        assertTrue(optionalIntygInfo.isPresent());

        IntygInfoDTO intygInfo = optionalIntygInfo.get();
        assertEquals("lisjp", intygInfo.getIntygType());
        assertEquals(1, intygInfo.getEvents().size());
        assertEquals(Source.INTYGSTJANSTEN, intygInfo.getEvents().get(0).getSource());
        assertFalse(intygInfo.isCreatedInWC());

        verify(intygInfoPersistenceService, times(1)).create(any());
    }

    @Test
    public void testGetIntygInfoFoundInWC() {
        mockUser();

        String intygId = "intygId";

        WcIntygInfo wcIntygInfo = new WcIntygInfo();
        wcIntygInfo.setCreatedInWC(true);
        wcIntygInfo.setIntygId(intygId);
        wcIntygInfo.setIntygType("lisjp");
        wcIntygInfo.setDraftCreated(LocalDateTime.now());
        wcIntygInfo.getEvents().add(new IntygInfoEvent(Source.WEBCERT, LocalDateTime.now(), IntygInfoEventType.IS001));

        when(wcIntegrationService.getIntygInfo(intygId)).thenReturn(wcIntygInfo);
        when(itIntegrationService.getIntygInfo(intygId)).thenReturn(null);

        Optional<IntygInfoDTO> optionalIntygInfo = intygInfoService.getIntygInfo(intygId);

        assertTrue(optionalIntygInfo.isPresent());

        IntygInfoDTO intygInfo = optionalIntygInfo.get();
        assertEquals("lisjp", intygInfo.getIntygType());
        assertEquals(1, intygInfo.getEvents().size());
        assertEquals(Source.WEBCERT, intygInfo.getEvents().get(0).getSource());
        assertTrue(intygInfo.isCreatedInWC());

        verify(intygInfoPersistenceService, times(1)).create(any());
    }

    @Test
    public void testGetIntygInfo() {
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

        when(wcIntegrationService.getIntygInfo(intygId)).thenReturn(wcIntygInfo);
        when(itIntegrationService.getIntygInfo(intygId)).thenReturn(itIntygInfo);

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

    @Test
    public void testGetIntygInfoDateNull() {
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

        when(wcIntegrationService.getIntygInfo(intygId)).thenReturn(wcIntygInfo);
        when(itIntegrationService.getIntygInfo(intygId)).thenReturn(itIntygInfo);

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

        IntygsadminUser user = new IntygsadminUser(userEntity, null, null);
        when(userService.getActiveUser()).thenReturn(user);
    }
}
