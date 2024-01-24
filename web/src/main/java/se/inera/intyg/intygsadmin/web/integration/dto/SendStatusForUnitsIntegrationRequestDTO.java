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

package se.inera.intyg.intygsadmin.web.integration.dto;

import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

public class SendStatusForUnitsIntegrationRequestDTO {

    private List<String> unitIds;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<NotificationStatusEnum> status;
    private LocalDateTime activationTime;

    public static SendStatusForUnitsIntegrationRequestDTO create(List<String> unitIds, LocalDateTime start, LocalDateTime end,
        List<NotificationStatusEnum> status,
        LocalDateTime activationTime) {
        final var response = new SendStatusForUnitsIntegrationRequestDTO();

        response.unitIds = unitIds;
        response.start = start;
        response.end = end;
        response.status = status;
        response.activationTime = activationTime;

        return response;
    }

    public List<String> getUnitIds() {
        return unitIds;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public List<NotificationStatusEnum> getStatus() {
        return status;
    }

    public LocalDateTime getActivationTime() {
        return activationTime;
    }

}
