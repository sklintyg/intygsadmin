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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.PPIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.PrivatePractitioner;

@ExtendWith(MockitoExtension.class)
class PrivatePractitionerServiceImplTest {

    @Mock
    private PPIntegrationRestService ppIntegrationRestService;
    @Mock
    private ITIntegrationRestService itIntegrationRestService;

    @InjectMocks
    private PrivatePractitionerServiceImpl privatePractitionerService;

    private static final String YES = "Ja";
    private static final String NO = "Nej";
    private static final String COUNT_FAILURE_MESSAGE = "Uppgift om utfärdade intyg kunde inte hämtas. Prova igen om en stund.";

    private static final String NAME = "Steve Vessla";
    private static final String EMAIL = "email@example.se";
    private static final String PERSON_ID = "191212121212";
    private static final String HSA_ID = "SE23210000055-TEST5";
    private static final String CARE_PROVIDER_NAME = "Vårdgivare Vesslan";
    private static final LocalDateTime REGISTRATION_DATE = LocalDateTime.now();


    @Nested
    public class testGetPrivatePractitioner {

        @Test
        public void shouldReturnNotFoundWhenNoPrivatePractitioner() {
            when(ppIntegrationRestService.getPrivatePractitioner(HSA_ID)).thenReturn(null);

            final var response = privatePractitionerService.getPrivatePractitioner(HSA_ID);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        public void shouldReturnHasCertificatesNoWhenCertificateCountIsZero() {
            when(ppIntegrationRestService.getPrivatePractitioner(PERSON_ID)).thenReturn(getPrivatePractitioner());
            when(itIntegrationRestService.getCertificateCount(HSA_ID)).thenReturn(0);

            final var response = privatePractitionerService.getPrivatePractitioner(PERSON_ID);

            assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(NO, Objects.requireNonNull(response.getBody()).getHasCertificates())
            );
        }

        @Test
        public void shouldReturnHasCertificatesYesWhenCertificateCountIsNotZero() {
            when(ppIntegrationRestService.getPrivatePractitioner(HSA_ID)).thenReturn(getPrivatePractitioner());
            when(itIntegrationRestService.getCertificateCount(HSA_ID)).thenReturn(1);

            final var response = privatePractitionerService.getPrivatePractitioner(HSA_ID);

            assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(YES, Objects.requireNonNull(response.getBody()).getHasCertificates())
            );
        }

        @Test
        public void shouldReturnErrorMessageStringWhenCertificateCountIsNull() {
            when(ppIntegrationRestService.getPrivatePractitioner(HSA_ID)).thenReturn(getPrivatePractitioner());
            when(itIntegrationRestService.getCertificateCount(HSA_ID)).thenReturn(null);

            final var response = privatePractitionerService.getPrivatePractitioner(HSA_ID);

            assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(COUNT_FAILURE_MESSAGE, Objects.requireNonNull(response.getBody()).getHasCertificates())
            );
        }

        @Test
        public void shouldCopyAllFieldsFromPrivatePractitionerToDto() {
            when(ppIntegrationRestService.getPrivatePractitioner(HSA_ID)).thenReturn(getPrivatePractitioner());
            when(itIntegrationRestService.getCertificateCount(HSA_ID)).thenReturn(null);

            final var response = privatePractitionerService.getPrivatePractitioner(HSA_ID);

            final var dto = Objects.requireNonNull(response.getBody());

            assertAll(
                () -> assertEquals(HSA_ID, dto.getHsaId()),
                () -> assertEquals(PERSON_ID, dto.getPersonId()),
                () -> assertEquals(EMAIL, dto.getEmail()),
                () -> assertEquals(NAME, dto.getName()),
                () -> assertEquals(CARE_PROVIDER_NAME, dto.getCareproviderName()),
                () -> assertEquals(REGISTRATION_DATE, dto.getRegistrationDate())
            );
        }
    }

    @Nested
    public class testGetPrivatePractitionerFile {

        @Test
        public void shouldReturnNoContentIfListEmptyList() {
            when(ppIntegrationRestService.getAllPrivatePractitioners()).thenReturn(Collections.emptyList());

            final var response = privatePractitionerService.getPrivatePractitionerFile();

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        public void shouldAddContentDispositionHeaderToResponse() {
            final var privatePractitionerList = List.of(getPrivatePractitioner());
            when(ppIntegrationRestService.getAllPrivatePractitioners()).thenReturn(privatePractitionerList);

            final var response = privatePractitionerService.getPrivatePractitionerFile();

            assertAll(
                () -> assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION)),
                () -> assertTrue(Objects.requireNonNull(
                    response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment; filename=privatlakare.xlsx"))
            );
        }
    }

    private PrivatePractitioner getPrivatePractitioner() {
        final var privatePractitioner = new PrivatePractitioner();
        privatePractitioner.setHsaId(HSA_ID);
        privatePractitioner.setPersonId(PERSON_ID);
        privatePractitioner.setEmail(EMAIL);
        privatePractitioner.setName(NAME);
        privatePractitioner.setCareproviderName(CARE_PROVIDER_NAME);
        privatePractitioner.setRegistrationDate(REGISTRATION_DATE);
        return privatePractitioner;
    }
}
