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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;

@Service
@RequiredArgsConstructor
public class SendStatusForCareGiverServiceImpl implements SendStatusForCareGiverService {

    private final WCIntegrationRestService wcIntegrationRestService;
    private final SendNotificationRequestValidator sendNotificationRequestValidator;

    @Value("${timeinterval.maxdays.caregiver:1}")
    private int maxTimeInterval;

    @Value("${timelimit.daysback.start:365}")
    private int maxDaysBackStartDate;

    @Override
    public Integer send(String careGiverId, SendStatusForCareGiverRequestDTO request) {
        sendNotificationRequestValidator.validateId(careGiverId);
        sendNotificationRequestValidator.validateDate(request.getStart(), request.getEnd(), maxTimeInterval, maxDaysBackStartDate);

        final var integrationRequest = SendStatusForCareGiverIntegrationRequestDTO.builder()
            .careGiverId(careGiverId)
            .start(request.getStart())
            .end(request.getEnd())
            .activationTime(request.getActivationTime())
            .statuses(request.getStatuses())
            .build();

        final var response = wcIntegrationRestService.sendStatusForCareGiver(integrationRequest);
        return response.getCount();
    }

    @Override
    public CountStatusesDTO count(String caregiverId, CountStatusesForCareGiverRequestDTO request) {
        final var integrationRequest = CountStatusesForCareGiverIntegrationRequestDTO.builder()
            .careGiverId(request.getCareGiverId())
            .start(request.getStart())
            .end(request.getEnd())
            .statuses(request.getStatuses())
            .build();

        final var response = wcIntegrationRestService.countStatusesForCareGiver(integrationRequest);
        return CountStatusesDTO.builder()
            .count(response.getCount())
            .max(response.getMax())
            .build();
    }
}
