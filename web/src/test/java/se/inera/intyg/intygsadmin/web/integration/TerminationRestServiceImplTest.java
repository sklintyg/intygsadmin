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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@ExtendWith(MockitoExtension.class)
class TerminationRestServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TerminationRestServiceImpl terminationRestService;

    @BeforeEach
    public void init(){
        ReflectionTestUtils.setField(terminationRestService,"terminationServiceUrl", "Host");
    }

    @Test
    void getDataExports() {
        DataExportResponse[] dataExportResponses = new DataExportResponse[1];
        when(restTemplate.getForObject(anyString(), any())).thenReturn(dataExportResponses);

        assertNotNull(terminationRestService.getDataExports());

        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

    @Test
    void shouldThrowWhenRestClientFailure() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("RestClientException"));

        assertThrows(RestClientException.class, () -> terminationRestService.getDataExports());
    }

    @Test
    void shouldThrowWhenFetchedTerminationsAreNull() {
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> terminationRestService.getDataExports());
    }

    @Test
    void createDataExport() {
        CreateDataExport createDataExport = new CreateDataExport();

        when(restTemplate.postForObject(anyString(), any(CreateDataExport.class), any())).thenReturn(new DataExportResponse());

        assertNotNull(terminationRestService.createDataExport(createDataExport));
        verify(restTemplate, times(1)).postForObject(anyString(), any(CreateDataExport.class), any());
    }

    @Test
    void eraseDataExport() {
        String terminationId = "201d403d-7bcb-4017-a529-0309bb6693a2";
        String responseStatus = "Avslutad";
        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(responseStatus);

        assertNotNull(terminationRestService.eraseDataExport(terminationId));

        verify(restTemplate, times(1)).postForObject(anyString(), any(), any());
    }
}