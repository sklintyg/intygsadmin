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
package se.inera.intyg.intygsadmin.web.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.TRACE_ID_KEY;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClientException;
import se.inera.intyg.intygsadmin.web.controller.dto.UpdateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@ExtendWith(MockitoExtension.class)
class TerminationRestServiceImplTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private TerminationRestServiceImpl terminationRestService;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(terminationRestService, "terminationServiceUrl", "Host");
    }

    @Nested
    class GetDataExportsTest {

        private RequestHeadersUriSpec requestBodyUriSpec;
        private ResponseSpec responseSpec;

        @BeforeEach
        void setUp() {
            requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.get()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("Host/api/v1/terminations")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void getDataExports() {
            doReturn(List.of()).when(responseSpec).body(any(ParameterizedTypeReference.class));

            assertNotNull(terminationRestService.getDataExports());
        }

        @Test
        void shouldThrowException() {
            doThrow(new RestClientException("Error!")).when(responseSpec).body(any(ParameterizedTypeReference.class));

            assertThrows(RestClientException.class, () -> terminationRestService.getDataExports());
        }
    }

    @Nested
    class CreateDataExportTest {

        private RequestBodyUriSpec requestBodyUriSpec;
        private ResponseSpec responseSpec;

        @BeforeEach
        void setUp() {
            requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(eq("Host/api/v1/terminations"))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(CreateDataExport.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void createDataExport() {
            final var createDataExport = new CreateDataExport();

            doReturn(new DataExportResponse()).when(responseSpec).body(DataExportResponse.class);

            assertNotNull(terminationRestService.createDataExport(createDataExport));
        }

        @Test
        void shouldThrowException() {
            final var createDataExport = new CreateDataExport();

            doThrow(new RestClientException("Error!")).when(responseSpec).body(DataExportResponse.class);

            assertThrows(RestClientException.class, () -> terminationRestService.createDataExport(createDataExport));
        }
    }

    @Nested
    class UpdateDataExportTest {

        private RequestBodyUriSpec requestBodyUriSpec;
        private ResponseSpec responseSpec;
        private String terminationId;

        @BeforeEach
        void setUp() {
            requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            terminationId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(eq("Host/api/v1/terminations/" + terminationId))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(UpdateDataExportDTO.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void shouldUpdateDataExport() {
            final var updateDataExportDTO = new UpdateDataExportDTO();

            doReturn(new DataExportResponse()).when(responseSpec).body(DataExportResponse.class);

            assertNotNull(terminationRestService.updateDataExport(terminationId, updateDataExportDTO));
        }

        @Test
        void shouldThrowException() {
            final var updateDataExportDTO = new UpdateDataExportDTO();

            doThrow(new RestClientException("Error!")).when(responseSpec).body(DataExportResponse.class);

            assertThrows(RestClientException.class, () -> terminationRestService.updateDataExport(terminationId, updateDataExportDTO));
        }
    }

    @Nested
    class EraseDataExportTest {

        private RequestBodyUriSpec requestBodyUriSpec;
        private ResponseSpec responseSpec;
        private String terminationId;

        @BeforeEach
        void setUp() {
            requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            terminationId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(eq("Host/api/v1/terminations/" + terminationId + "/erase"))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void eraseDataExport() {
            doReturn("Avslutad").when(responseSpec).body(String.class);

            assertNotNull(terminationRestService.eraseDataExport(terminationId));
        }

        @Test
        void shouldThrowException() {
            doThrow(new RestClientException("Error!")).when(responseSpec).body(String.class);

            assertThrows(RestClientException.class, () -> terminationRestService.eraseDataExport(terminationId));
        }
    }

    @Nested
    class ResendDataExportKeyTest {

        private RequestBodyUriSpec requestBodyUriSpec;
        private ResponseSpec responseSpec;
        private String terminationId;

        @BeforeEach
        void setUp() {
            requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            terminationId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(eq("Host/api/v1/terminations/" + terminationId + "/resendpassword"))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void resendDataExportKey() {
            doReturn("Kryptonyckel skickad igen").when(responseSpec).body(String.class);

            assertNotNull(terminationRestService.resendDataExportKey(terminationId));
        }

        @Test
        void shouldThrowException() {
            doThrow(new RestClientException("Error!")).when(responseSpec).body(String.class);

            assertThrows(RestClientException.class, () -> terminationRestService.resendDataExportKey(terminationId));
        }
    }
}
