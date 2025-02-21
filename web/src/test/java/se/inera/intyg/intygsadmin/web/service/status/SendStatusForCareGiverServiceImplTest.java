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

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationResponseDTO;

@ExtendWith(MockitoExtension.class)
class SendStatusForCareGiverServiceImplTest {

    @Mock
    private WCIntegrationRestService wcIntegrationRestService;

    @Mock
    private SendNotificationRequestValidator sendNotificationRequestValidator;

    @InjectMocks
    private SendStatusForCareGiverServiceImpl sendStatusForCareGiverServiceImpl;

    @Test
    void shouldSendStatusForCareGiver() {
        final var request = SendStatusForCareGiverRequestDTO.builder()
            .status(List.of(NotificationStatusEnum.FAILURE))
            .start(LocalDateTime.now().minusMonths(1))
            .end(LocalDateTime.now())
            .activationTime(LocalDateTime.now())
            .build();

        final var expected = SendStatusIntegrationResponseDTO.builder()
            .count(1)
            .build();

        when(wcIntegrationRestService.sendStatusForCareGiver(any(SendStatusForCareGiverIntegrationRequestDTO.class)))
            .thenReturn(expected);

        final var response = sendStatusForCareGiverServiceImpl.send("careGiverId", request);

        assertEquals(1, response);
    }

    @Test
    void shouldCountStatusForCareGiver() {
        final var request = CountStatusesForCareGiverRequestDTO.builder()
            .careGiverId("careGiverId")
            .status(List.of(NotificationStatusEnum.FAILURE))
            .start(LocalDateTime.now().minusMonths(1))
            .end(LocalDateTime.now())
            .build();

        final var expected = SendStatusIntegrationResponseDTO.builder()
            .count(1)
            .build();

        when(wcIntegrationRestService.countStatusesForCareGiver(any(
            CountStatusesForCareGiverIntegrationRequestDTO.class)))
            .thenReturn(expected);

        final var response = sendStatusForCareGiverServiceImpl.count("careGiverId", request);

        assertEquals(1, response);
    }
}
