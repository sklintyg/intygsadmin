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
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForCertificatesRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCertificatesRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForCertificatesIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCertificatesIntegrationRequestDTO;

@Service
@RequiredArgsConstructor
public class SendStatusForCertificatesServiceImpl implements SendStatusForCertificatesService {

    private final WCIntegrationRestService wcIntegrationRestService;
    private final SendNotificationRequestValidator sendNotificationRequestValidator;

    @Override
    public Integer send(SendStatusForCertificatesRequestDTO request) {
        sendNotificationRequestValidator.validateIds(request.getCertificateIds());

        final var integrationRequest = SendStatusForCertificatesIntegrationRequestDTO.builder()
            .certificateIds(request.getCertificateIds())
            .status(request.getStatus())
            .build();

        final var response = wcIntegrationRestService.sendStatusForCertificates(integrationRequest);
        return response.getCount();
    }

    @Override
    public Integer count(CountStatusesForCertificatesRequestDTO request) {
        final var integrationRequest = CountStatusesForCertificatesIntegrationRequestDTO.builder()
            .certificateIds(request.getCertificateIds())
            .status(request.getStatus())
            .build();

        final var response = wcIntegrationRestService.countStatusesForCertificates(integrationRequest);
        return response.getCount();
    }
}
