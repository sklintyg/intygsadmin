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

package se.inera.intyg.intygsadmin.web.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

class IntygAvslutRestServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IntygAvslutRestServiceImpl intygAvslutRestService;

    @Test
    void getDataExports() {
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new ArrayList<>());

        assertNotNull(intygAvslutRestService.getDataExports());

        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }


    @Test
    void createDataExport() {
        CreateDataExport createDataExport = new CreateDataExport();

        when(restTemplate.postForObject(anyString(), any(CreateDataExportDTO.class), any())).thenReturn(new DataExportResponse());

        assertNotNull(intygAvslutRestService.createDataExport(createDataExport));
        verify(restTemplate, times(1)).postForObject(anyString(), any(CreateDataExportDTO.class), any());
    }
}