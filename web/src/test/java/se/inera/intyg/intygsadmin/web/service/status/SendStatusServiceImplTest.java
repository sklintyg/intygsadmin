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

package se.inera.intyg.intygsadmin.web.service.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationResponseDTO;

@ExtendWith(MockitoExtension.class)
class SendStatusServiceImplTest {

    @Mock
    private WCIntegrationRestService wcIntegrationRestService;

    @Mock
    private SendNotificationRequestValidator sendNotificationRequestValidator;

    @InjectMocks
    private SendStatusServiceImpl sendStatusServiceImpl;

    @Test
    void shouldSendStatus() {
        final var expected = SendStatusIntegrationResponseDTO.builder()
            .count(1)
            .build();

        when(wcIntegrationRestService.sendStatus(any(SendStatusIntegrationRequestDTO.class)))
            .thenReturn(expected);

        final var response = sendStatusServiceImpl.send("statusId");

        assertEquals(1, response);
    }
}
