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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.IntygAvslutRestService;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@ExtendWith(MockitoExtension.class)
class IntygAvslutServiceImplTest {

    @Mock
    private IntygAvslutRestService intygAvslutRestService;

    @InjectMocks
    private IntygAvslutServiceImpl intygAvslutService;

    @Test
    void testGetDataExports() {
        when(intygAvslutRestService.getDataExports()).thenReturn(new ArrayList<>());

        assertNotNull(intygAvslutService.getDataExports());

        verify(intygAvslutRestService, times(1)).getDataExports();
    }

    @Test
    void testCreateDataExport() {
        CreateDataExportDTO createDataExportDTO = new CreateDataExportDTO();

        when(intygAvslutRestService.createDataExport(any(CreateDataExport.class))).thenReturn(new DataExportResponse());

        assertNotNull(intygAvslutService.createDataExport(createDataExportDTO));
        verify(intygAvslutRestService, times(1)).createDataExport(any(CreateDataExport.class));
    }
}