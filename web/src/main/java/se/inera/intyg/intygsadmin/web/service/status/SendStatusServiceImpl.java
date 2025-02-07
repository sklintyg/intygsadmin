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

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationRequestDTO;

@Service
public class SendStatusServiceImpl implements SendStatusService {

    private final WCIntegrationRestService wcIntegrationRestService;

    public SendStatusServiceImpl(WCIntegrationRestService wcIntegrationRestService) {
        this.wcIntegrationRestService = wcIntegrationRestService;
    }

    @Override
    public Integer send(String statusId) {
        final var request = SendStatusIntegrationRequestDTO.builder()
            .statusId(statusId)
            .build();

        final var response = wcIntegrationRestService.sendStatus(request);
        return response.getCount();
    }
}
