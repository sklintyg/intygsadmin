/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.TerminationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@ExtendWith(MockitoExtension.class)
class TerminationServiceImplTest {

    @Mock
    private TerminationRestService terminationRestService;

    @Captor
    ArgumentCaptor<CreateDataExport> createDataExportArgumentCaptor;

    @Mock
    private UserService userService;

    @InjectMocks
    private TerminationServiceImpl terminationService;

    @Test
    void testGetDataExports() {
        when(terminationRestService.getDataExports()).thenReturn(new ArrayList<>());

        assertNotNull(terminationService.getDataExports());

        verify(terminationRestService, times(1)).getDataExports();
    }

    @Test
    void testCreateDataExport() {

        UserEntity userEntity = new UserEntity();
        userEntity.setName("test");
        userEntity.setEmployeeHsaId("Nmågot hsa-id");
        AuthenticationMethod authenticationMethod= AuthenticationMethod.FAKE;
        OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken("Token");
        IntygsadminUser intygsadminUser = new IntygsadminUser(userEntity, authenticationMethod, oAuth2AccessToken);

        CreateDataExportDTO createDataExportDTO = new CreateDataExportDTO();
        createDataExportDTO.setHsaId("1");
        createDataExportDTO.setPersonId("2");
        createDataExportDTO.setPhoneNumber("3");
        createDataExportDTO.setOrganizationNumber("4");

        when(userService.getActiveUser()).thenReturn(intygsadminUser);
        when(terminationRestService.createDataExport(any(CreateDataExport.class))).thenReturn(new DataExportResponse());

        assertNotNull(terminationService.createDataExport(createDataExportDTO));

        verify(userService, times(1)).getActiveUser();
        verify(terminationRestService, times(1)).createDataExport(any(CreateDataExport.class));
        verify(terminationRestService).createDataExport(createDataExportArgumentCaptor.capture());

        CreateDataExport createdCreateDataExport = createDataExportArgumentCaptor.getValue();
        assertEquals(createdCreateDataExport.getCreatorName() , userEntity.getName());
        assertEquals(createdCreateDataExport.getCreatorHSAId() , userEntity.getEmployeeHsaId());

        assertEquals(createDataExportDTO.getHsaId() , createDataExportDTO.getHsaId());
        assertEquals(createDataExportDTO.getPersonId() , createDataExportDTO.getPersonId());
        assertEquals(createDataExportDTO.getPhoneNumber() , createDataExportDTO.getPhoneNumber());
        assertEquals(createDataExportDTO.getOrganizationNumber() , createDataExportDTO.getOrganizationNumber());
    }
}