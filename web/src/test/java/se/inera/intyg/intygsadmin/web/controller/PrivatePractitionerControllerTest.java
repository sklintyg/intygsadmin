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
package se.inera.intyg.intygsadmin.web.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import se.inera.intyg.intygsadmin.web.service.PrivatePractitionerService;

@ExtendWith(MockitoExtension.class)
class PrivatePractitionerControllerTest {

    @Mock
    private PrivatePractitionerService privatePractitionerService;

    @InjectMocks
    private PrivatePractitionerController privatePractitionerController;

    @Test
    public void shouldReturnStatusNoContentIfNoPrivatePractitioners() throws IOException {
        when(privatePractitionerService.getPrivatePractitionerFile()).thenReturn(null);

        final var response = privatePractitionerController.getPrivatePractitionerFile();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void shouldReturnStatusInternalServerErrorOnIOExeptkon() throws IOException {
        when(privatePractitionerService.getPrivatePractitionerFile()).thenThrow(new IOException("IOException"));

        final var response = privatePractitionerController.getPrivatePractitionerFile();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldReturnOkOnNonNullResponse() throws IOException {
        final var byteArray = "Private practitioner file".getBytes();
        when(privatePractitionerService.getPrivatePractitionerFile()).thenReturn(byteArray);

        final var response = privatePractitionerController.getPrivatePractitionerFile();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(byteArray, response.getBody())
        );
    }
}
