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
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForUnitsRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForUnitsRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForUnitsIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForUnitsIntegrationRequestDTO;

@Service
@RequiredArgsConstructor
public class SendStatusForUnitsServiceImpl implements SendStatusForUnitsService {

    private final WCIntegrationRestService wcIntegrationRestService;
    private final SendNotificationRequestValidator sendNotificationRequestValidator;

    @Value("${timeinterval.maxdays.unit:7}")
    private int maxTimeInterval;

    @Value("${timelimit.daysback.start:365}")
    private int maxDaysBackStartDate;

    @Override
    public Integer send(SendStatusForUnitsRequestDTO request) {
        sendNotificationRequestValidator.validateIds(request.getUnitIds());
        sendNotificationRequestValidator.validateDate(request.getStart(), request.getEnd(),
            maxTimeInterval, maxDaysBackStartDate);

        final var integrationRequest = SendStatusForUnitsIntegrationRequestDTO.builder()
            .unitIds(request.getUnitIds())
            .status(request.getStatus())
            .start(request.getStart())
            .end(request.getEnd())
            .activationTime(request.getActivationTime())
            .build();

        final var response = wcIntegrationRestService.sendStatusForUnits(integrationRequest);
        return response.getCount();
    }

    @Override
    public Integer count(CountStatusesForUnitsRequestDTO request) {
        final var integrationRequest = CountStatusesForUnitsIntegrationRequestDTO.builder()
            .unitIds(request.getUnitIds())
            .start(request.getStart())
            .end(request.getEnd())
            .status(request.getStatus())
            .build();

        final var response = wcIntegrationRestService.countStatusesForUnits(integrationRequest);
        return response.getCount();
    }
}
