/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForTimePeriodIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationResponseDTO;

@ExtendWith(MockitoExtension.class)
class SendStatusForTimePeriodServiceImplTest {

    @Mock
    private WCIntegrationRestService wcIntegrationRestService;
    @InjectMocks
    private SendStatusForTimePeriodServiceImpl sendStatusForTimePeriodServiceImpl;

    @Test
    void sendStatusForTimePeriod() {

        final var request = SendStatusForTimePeriodIntegrationRequestDTO.create(
            LocalDateTime.now(),
            LocalDateTime.now(),
            NotificationStatusEnum.FAILURE,
            LocalDateTime.now()
        );

        when(wcIntegrationRestService.sendStatusForTimePeriod(any(SendStatusForTimePeriodIntegrationRequestDTO.class))).thenReturn(
            SendStatusIntegrationResponseDTO.create(1));

        final var response = sendStatusForTimePeriodServiceImpl.send(request);

        assertEquals(1, response);

    }

}