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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCertificatesRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForUnitsRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusResponseDTO;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCareGiverService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCertificatesService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForUnitsService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusService;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final SendStatusService sendStatusService;
    private final SendStatusForCertificatesService sendStatusForCertificatesService;
    private final SendStatusForUnitsService sendStatusForUnitsService;
    private final SendStatusForCareGiverService sendStatusForCareGiverService;

    public StatusController(SendStatusService sendStatusService, SendStatusForCertificatesService sendStatusForCertificatesService,
        SendStatusForUnitsService sendStatusForUnitsService, SendStatusForCareGiverService sendStatusForCareGiverService) {
        this.sendStatusService = sendStatusService;
        this.sendStatusForCertificatesService = sendStatusForCertificatesService;
        this.sendStatusForUnitsService = sendStatusForUnitsService;
        this.sendStatusForCareGiverService = sendStatusForCareGiverService;
    }

    @PostMapping("/{statusId}")
    public SendStatusResponseDTO sendStatus(@PathVariable String statusId) {
        final var response = sendStatusService.send(statusId);
        return SendStatusResponseDTO.create(response);
    }

    @PostMapping("/certificates")
    public SendStatusResponseDTO sendStatusForCertificate(@RequestBody SendStatusForCertificatesRequestDTO request) {
        final var response = sendStatusForCertificatesService.send(request);
        return SendStatusResponseDTO.create(response);
    }

    @PostMapping("/units")
    public SendStatusResponseDTO sendStatusForUnits(@RequestBody SendStatusForUnitsRequestDTO request) {
        final var response = sendStatusForUnitsService.send(request);
        return SendStatusResponseDTO.create(response);
    }

    @PostMapping("/{careGiverId}")
    public SendStatusResponseDTO sendStatusForCareGiver(@PathVariable String careGiverId,
        @RequestBody SendStatusForCareGiverRequestDTO request) {
        final var response = sendStatusForCareGiverService.send(careGiverId, request);
        return SendStatusResponseDTO.create(response);
    }

}
