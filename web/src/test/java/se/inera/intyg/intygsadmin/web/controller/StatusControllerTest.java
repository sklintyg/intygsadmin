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

package se.inera.intyg.intygsadmin.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusResponseDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCertificatesIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForTimePeriodIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForUnitsIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCareGiverService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCertificatesService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForTimePeriodService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForUnitsService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusService;

@ExtendWith(MockitoExtension.class)
class StatusControllerTest {

    public static final String STATUS_ID = "statusId";
    @Mock
    private SendStatusService sendStatusService;

    @Mock
    private SendStatusForCertificatesService sendStatusForCertificatesService;

    @Mock
    SendStatusForUnitsService sendStatusForUnitsService;

    @Mock
    private SendStatusForCareGiverService sendStatusForCareGiverService;

    @Mock
    private SendStatusForTimePeriodService sendStatusForTimePeriodService;

    @InjectMocks
    private StatusController statusController;

    @Test
    void shouldSendStatus() {
        final var sendStatusResponse = SendStatusResponseDTO.create(1);
        when(sendStatusService.send(STATUS_ID)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatus(STATUS_ID).getCount());
    }

    @Test
    void shouldSetSendStatusForCertificates() {
        final var request = SendStatusForCertificatesIntegrationRequestDTO.create(
            List.of("certificateId1", "certificateId2"),
            List.of(NotificationStatusEnum.FAILURE),
            LocalDateTime.now()
        );

        final var sendStatusResponse = SendStatusResponseDTO.create(1);
        when(sendStatusForCertificatesService.send(request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForCertificate(request).getCount());
    }

    @Test
    void shouldSetSendStatusForUnits() {
        final var request = SendStatusForUnitsIntegrationRequestDTO.create(
            List.of("unitId1", "unitId2"),
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of(NotificationStatusEnum.FAILURE),
            LocalDateTime.now()
        );

        final var sendStatusResponse = SendStatusResponseDTO.create(1);
        when(sendStatusForUnitsService.send(request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForUnits(request).getCount());
    }

    @Test
    void shouldSetSendStatusForCareGiver() {
        final var request = SendStatusForCareGiverIntegrationRequestDTO.create(
            "careGiverId",
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of(NotificationStatusEnum.FAILURE),
            LocalDateTime.now()
        );

        final var sendStatusResponse = SendStatusResponseDTO.create(1);
        when(sendStatusForCareGiverService.send("careGiverId", request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForCareGiver("careGiverId", request).getCount());
    }

    @Test
    void shouldSetSendStatusForTimePeriod() {
        final var request = SendStatusForTimePeriodIntegrationRequestDTO.create(
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of(NotificationStatusEnum.FAILURE),
            LocalDateTime.now()
        );

        final var sendStatusResponse = SendStatusResponseDTO.create(1);
        when(sendStatusForTimePeriodService.send(request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForTimePeriod(request).getCount());
    }
}