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

package se.inera.intyg.intygsadmin.web.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.service.TerminationService;

@ExtendWith(MockitoExtension.class)
class DataExportControllerTest {

    @Mock
    private TerminationService terminationService;

    @InjectMocks
    private DataExportController dataExportController;

    @Mock
    private Pageable pageable;

    @ParameterizedTest
    @ValueSource(strings = {"createdAt", "creatorName", "terminationId", "status", "careProviderHsaId", "organizationNumber", "representativePersonId", "representativePhoneNumber"})
    void listDataExports(String collumnName) {
        Sort sort = Sort.by(Direction.DESC, collumnName);
        when(pageable.getSort()).thenReturn(sort);
        when(terminationService.getDataExports()).thenReturn(new ArrayList<>());

        assertNotNull(dataExportController.listDataExports(pageable));

        verify(terminationService, times(1)).getDataExports();
    }

    @Test
    void createDataExport() {
        CreateDataExportDTO createDataExportDTO = new CreateDataExportDTO();
        when(terminationService.createDataExport(createDataExportDTO)).thenReturn(new DataExportResponse());

        assertNotNull(dataExportController.createDataExport(createDataExportDTO));

        verify(terminationService, times(1)).createDataExport(createDataExportDTO);
    }
}