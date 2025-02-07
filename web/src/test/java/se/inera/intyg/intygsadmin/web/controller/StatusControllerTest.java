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
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCertificatesRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForUnitsRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusResponseDTO;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCareGiverService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCertificatesService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForUnitsService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusService;

@ExtendWith(MockitoExtension.class)
class StatusControllerTest {

    private static final String STATUS_ID = "statusId";
    private static final LocalDateTime START = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime END = LocalDateTime.now();
    private static final LocalDateTime ACTIVATION_TIME = LocalDateTime.now().plusDays(1);
    private static final List<NotificationStatusEnum> STATUS_LIST = List.of(NotificationStatusEnum.FAILURE);
    private static final String CARE_GIVER_ID = "careGiverId";

    @Mock
    private SendStatusService sendStatusService;

    @Mock
    private SendStatusForCertificatesService sendStatusForCertificatesService;

    @Mock
    SendStatusForUnitsService sendStatusForUnitsService;

    @Mock
    private SendStatusForCareGiverService sendStatusForCareGiverService;


    @InjectMocks
    private StatusController statusController;

    @Test
    void shouldSendStatus() {
        final var sendStatusResponse = SendStatusResponseDTO.builder()
            .count(1)
            .build();
        when(sendStatusService.send(STATUS_ID)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatus(STATUS_ID).getCount());
    }

    @Test
    void shouldSetSendStatusForCertificates() {
        final var request = SendStatusForCertificatesRequestDTO.builder()
            .certificateIds(List.of("certificateId"))
            .status(STATUS_LIST)
            .activationTime(LocalDateTime.now())
            .build();

        final var sendStatusResponse = SendStatusResponseDTO.builder()
            .count(1)
            .build();

        when(sendStatusForCertificatesService.send(request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForCertificate(request).getCount());
    }

    @Test
    void shouldSetSendStatusForUnits() {
        final var request = SendStatusForUnitsRequestDTO.builder()
            .unitIds(List.of("unitId"))
            .status(STATUS_LIST)
            .start(START)
            .end(END)
            .activationTime(ACTIVATION_TIME)
            .build();

        final var sendStatusResponse = SendStatusResponseDTO.builder()
            .count(1)
            .build();

        when(sendStatusForUnitsService.send(request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForUnits(request).getCount());
    }

    @Test
    void shouldSetSendStatusForCareGiver() {
        final var request = SendStatusForCareGiverRequestDTO.builder()
            .careGiverId(CARE_GIVER_ID)
            .start(START)
            .end(END)
            .activationTime(ACTIVATION_TIME)
            .status(STATUS_LIST)
            .build();

        final var sendStatusResponse = SendStatusResponseDTO.builder()
            .count(1)
            .build();

        when(sendStatusForCareGiverService.send(CARE_GIVER_ID, request)).thenReturn(sendStatusResponse.getCount());
        assertEquals(1, statusController.sendStatusForCareGiver(CARE_GIVER_ID, request).getCount());
    }

}
