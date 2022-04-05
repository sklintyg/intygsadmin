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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import se.inera.intyg.intygsadmin.persistence.entity.DataExportEntity;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.DataExportStatus;
import se.inera.intyg.intygsadmin.persistence.service.IntygAvslutPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportDTO;
import se.inera.intyg.intygsadmin.web.mapper.DataExportMapper;

@ExtendWith(MockitoExtension.class)
class IntygAvslutServiceImplTest {

    @Mock
    private IntygAvslutPersistenceService intygAvslutPersistenceService;
    @Mock
    private DataExportMapper dataExportMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private IntygAvslutServiceImpl intygAvslutService;

    @Test
    void testGetDataExports() {
        when(intygAvslutPersistenceService.findAll(any(Pageable.class))).thenReturn(new PageImpl<DataExportEntity>(new ArrayList<>()));
        when(dataExportMapper.toListDTO(any(List.class))).thenReturn(new ArrayList());

        assertNotNull(intygAvslutService.getDataExports(Pageable.unpaged()));

        verify(intygAvslutPersistenceService, times(1)).findAll(any(Pageable.class));
        verify(dataExportMapper, times(1)).toListDTO(any(List.class));
    }

    @Test
    void testGetDataExportStatuses() {
        assertThrows(NotImplementedException.class, () -> { intygAvslutService.getDataExportStatuses(UUID.randomUUID()); });
    }

    @Test
    void testDeleteUserData() {
        assertThrows(NotImplementedException.class, () -> { intygAvslutService.deleteUserData(UUID.randomUUID()); });
    }

    @Test
    void testUpdateDataExport() {
        DataExportDTO dataExportDTO = new DataExportDTO();
        DataExportEntity dataExportEntityIn = new DataExportEntity();
        DataExportEntity dataExportEntityOut = new DataExportEntity();

        when(dataExportMapper.toEntity(any(DataExportDTO.class))).thenReturn(dataExportEntityIn);
        when(intygAvslutPersistenceService.update(dataExportEntityIn)).thenReturn(dataExportEntityOut);
        when(dataExportMapper.toDTO(any(DataExportEntity.class))).thenReturn(dataExportDTO);

        assertNotNull(intygAvslutService.updateDataExport(UUID.randomUUID(), "personnr", "Phone"));

        verify(dataExportMapper).toEntity(any(DataExportDTO.class));
        verify(intygAvslutPersistenceService).update(dataExportEntityIn);
        verify(dataExportMapper).toDTO(any(DataExportEntity.class));
    }

    @Test
    void testCreateDataExport() {
        CreateDataExportDTO createDataExportDTO = new CreateDataExportDTO();
        DataExportDTO dataExportDTOIn = new DataExportDTO();
        DataExportDTO dataExportDTOOut = new DataExportDTO();
        DataExportEntity dataExportEntityIn = new DataExportEntity();
        DataExportEntity dataExportEntityOut = new DataExportEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Test Testsson");
        IntygsadminUser intygsadminUser = new IntygsadminUser(userEntity, AuthenticationMethod.FAKE, mock(OAuth2AccessToken.class));

        when(dataExportMapper.toEntity(createDataExportDTO)).thenReturn(dataExportDTOIn);
        when(userService.getActiveUser()).thenReturn(intygsadminUser);
        when(dataExportMapper.toEntity(dataExportDTOIn)).thenReturn(dataExportEntityIn);
        when(intygAvslutPersistenceService.add(dataExportEntityIn)).thenReturn(dataExportEntityOut);
        when(dataExportMapper.toDTO(dataExportEntityOut)).thenReturn(dataExportDTOOut);

        assertNotNull(intygAvslutService.createDataExport(createDataExportDTO));

        //Assert that fields has been set correct
        assertNull(dataExportDTOIn.getId());
        assertNotNull(dataExportDTOIn.getCreatedAt());
        assertEquals(dataExportDTOIn.getStatus(), DataExportStatus.CREATED);
        assertEquals(dataExportDTOIn.getAdministratorName(), userEntity.getName());

        verify(dataExportMapper).toEntity(createDataExportDTO);
        verify(userService).getActiveUser();
        verify(dataExportMapper).toEntity(dataExportDTOIn);
        verify(intygAvslutPersistenceService).add(dataExportEntityIn);
        verify(dataExportMapper).toDTO(dataExportEntityOut);
    }


    @Test
    void testDeleteExportRequest() {
        assertThrows(NotImplementedException.class, () -> { intygAvslutService.deleteExportRequest(UUID.randomUUID()); });
    }
}