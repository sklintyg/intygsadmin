package se.inera.intyg.intygsadmin.web.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.TRACE_ID_KEY;

import java.time.LocalDateTime;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesIntegrationResponseDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCertificatesIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForUnitsIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationResponseDTO;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@ExtendWith(MockitoExtension.class)
class WCIntegrationRestServiceImplTest {

    @Mock
    RestClient restClient;

    @InjectMocks
    private WCIntegrationRestServiceImpl wcIntegrationRestService;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(wcIntegrationRestService, "webcertUrl", "Host");
    }

    @Nested
    class SendStatusTest {

        private ResponseSpec responseSpec;
        private String statusId;

        @BeforeEach
        void setUp() {
            RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            statusId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("Host/internalapi/notification/" + statusId)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(SendStatusIntegrationRequestDTO.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void shouldSendStatus() {
            final var request = SendStatusIntegrationRequestDTO.builder()
                .statusId(statusId)
                .build();

            final var response = SendStatusIntegrationResponseDTO.builder()
                .count(1)
                .build();

            doReturn(response).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);
            assertNotNull(wcIntegrationRestService.sendStatus(request));
        }

        @Test
        void shouldThrowException() {
            final var request = SendStatusIntegrationRequestDTO.builder()
                .statusId(statusId)
                .build();

            doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);

            assertThrows(HttpClientErrorException.class, () -> wcIntegrationRestService.sendStatus(request));
        }
    }

    @Nested
    class SendStatusForCertificatesTest {

        private ResponseSpec responseSpec;
        private String certificateId;

        @BeforeEach
        void setUp() {
            RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            certificateId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("Host/internalapi/notification/certificates")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(SendStatusForCertificatesIntegrationRequestDTO.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void shouldSendStatusForCertificates() {
            final var request = SendStatusForCertificatesIntegrationRequestDTO.builder()
                .certificateIds(List.of(certificateId))
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .build();

            final var response = SendStatusIntegrationResponseDTO.builder()
                .count(1)
                .build();

            doReturn(response).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);
            assertNotNull(wcIntegrationRestService.sendStatusForCertificates(request));
        }

        @Test
        void shouldThrowException() {
            final var request = SendStatusForCertificatesIntegrationRequestDTO.builder()
                .certificateIds(List.of(certificateId))
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .build();

            doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);

            assertThrows(HttpClientErrorException.class, () -> wcIntegrationRestService.sendStatusForCertificates(request));
        }

    }

    @Nested
    class SendStatusForUnitsTest {

        private ResponseSpec responseSpec;
        private String unitId;

        @BeforeEach
        void setUp() {
            RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            unitId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("Host/internalapi/notification/units")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(SendStatusForUnitsIntegrationRequestDTO.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void shouldSendStatusForUnits() {
            final var request = SendStatusForUnitsIntegrationRequestDTO.builder()
                .unitIds(List.of(unitId))
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .activationTime(LocalDateTime.now())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

            final var response = SendStatusIntegrationResponseDTO.builder()
                .count(1)
                .build();

            doReturn(response).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);
            assertNotNull(wcIntegrationRestService.sendStatusForUnits(request));
        }

        @Test
        void shouldThrowException() {
            final var request = SendStatusForUnitsIntegrationRequestDTO.builder()
                .unitIds(List.of(unitId))
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .activationTime(LocalDateTime.now())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

            doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);

            assertThrows(HttpClientErrorException.class, () -> wcIntegrationRestService.sendStatusForUnits(request));
        }

    }

    @Nested
    class SendStatusForCareGiverTest {

        private ResponseSpec responseSpec;
        private String careGiverId;

        @BeforeEach
        void setUp() {
            RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            careGiverId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("Host/internalapi/notification/caregiver/" + careGiverId)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(SendStatusForCareGiverIntegrationRequestDTO.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void shouldSendStatusForCareGiver() {
            final var request = SendStatusForCareGiverIntegrationRequestDTO.builder()
                .careGiverId(careGiverId)
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .activationTime(LocalDateTime.now())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

            final var response = SendStatusIntegrationResponseDTO.builder()
                .count(1)
                .build();

            doReturn(response).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);
            assertNotNull(wcIntegrationRestService.sendStatusForCareGiver(request));
        }

        @Test
        void shouldThrowException() {
            final var request = SendStatusForCareGiverIntegrationRequestDTO.builder()
                .careGiverId(careGiverId)
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .activationTime(LocalDateTime.now())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

            doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(responseSpec).body(SendStatusIntegrationResponseDTO.class);

            assertThrows(HttpClientErrorException.class, () -> wcIntegrationRestService.sendStatusForCareGiver(request));
        }
    }

    @Nested
    class CountStatusForCareGiverTest {

        private ResponseSpec responseSpec;
        private String careGiverId;

        @BeforeEach
        void setUp() {
            RequestBodyUriSpec requestBodyUriSpec = mock(RequestBodyUriSpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);
            careGiverId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID_KEY, "traceId");
            MDC.put(SESSION_ID_KEY, "sessionId");

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri("Host/internalapi/notification/count/caregiver/" + careGiverId)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_TRACE_ID_HEADER, "traceId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.header(LOG_SESSION_ID_HEADER, "sessionId")).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.body(any(CountStatusesForCareGiverIntegrationRequestDTO.class))).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        void shouldCountStatusesForCareGiver() {
            final var request = CountStatusesForCareGiverIntegrationRequestDTO.builder()
                .careGiverId(careGiverId)
                .statuses(List.of(NotificationStatusEnum.FAILURE))
                .build();

            final var response = CountStatusesIntegrationResponseDTO.builder()
                .count(1)
                .max(1)
                .build();

            doReturn(response).when(responseSpec).body(CountStatusesIntegrationResponseDTO.class);
            assertNotNull(wcIntegrationRestService.countStatusesForCareGiver(request));
        }

    }
}
