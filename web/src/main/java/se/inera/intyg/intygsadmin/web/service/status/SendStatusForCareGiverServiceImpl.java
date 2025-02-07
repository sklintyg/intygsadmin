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

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;

@Service
public class SendStatusForCareGiverServiceImpl implements SendStatusForCareGiverService {

    private final WCIntegrationRestService wcIntegrationRestService;

    public SendStatusForCareGiverServiceImpl(WCIntegrationRestService wcIntegrationRestService) {
        this.wcIntegrationRestService = wcIntegrationRestService;
    }

    @Override
    public Integer send(String careGiverId, SendStatusForCareGiverRequestDTO request) {
        final var integrationRequest = SendStatusForCareGiverIntegrationRequestDTO.builder()
            .careGiverId(request.getCareGiverId())
            .start(request.getStart())
            .end(request.getEnd())
            .activationTime(request.getActivationTime())
            .status(request.getStatus())
            .build();

        final var response = wcIntegrationRestService.sendStatusForCareGiver(integrationRequest);
        return response.getCount();
    }
}
