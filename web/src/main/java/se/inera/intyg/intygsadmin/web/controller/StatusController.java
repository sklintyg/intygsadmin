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

package se.inera.intyg.intygsadmin.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesResponseDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForCertificatesRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.CountStatusesForUnitsRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCareGiverRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForCertificatesRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusForUnitsRequestDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SendStatusResponseDTO;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCareGiverService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForCertificatesService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusForUnitsService;
import se.inera.intyg.intygsadmin.web.service.status.SendStatusService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final SendStatusService sendStatusService;
    private final SendStatusForCertificatesService sendStatusForCertificatesService;
    private final SendStatusForUnitsService sendStatusForUnitsService;
    private final SendStatusForCareGiverService sendStatusForCareGiverService;

    @PostMapping("/{statusId}")
    public SendStatusResponseDTO sendStatus(@PathVariable String statusId) {
        final var response = sendStatusService.send(statusId);
        return SendStatusResponseDTO.builder()
            .count(response)
            .build();
    }

    @PostMapping("/certificates")
    public SendStatusResponseDTO sendStatusForCertificate(@RequestBody SendStatusForCertificatesRequestDTO request) {
        final var response = sendStatusForCertificatesService.send(request);
        return SendStatusResponseDTO.builder()
            .count(response)
            .build();
    }

    @PostMapping("/count/certificates")
    public CountStatusesResponseDTO countStatusesForCertificates(@RequestBody CountStatusesForCertificatesRequestDTO request) {
        final var response = sendStatusForCertificatesService.count(request);
        return CountStatusesResponseDTO.builder()
            .count(response.getCount())
            .max(response.getMax())
            .build();
    }

    @PostMapping("/units")
    public SendStatusResponseDTO sendStatusForUnits(@RequestBody SendStatusForUnitsRequestDTO request) {
        final var response = sendStatusForUnitsService.send(request);
        return SendStatusResponseDTO.builder()
            .count(response)
            .build();
    }

    @PostMapping("/count/units")
    public CountStatusesResponseDTO countStatusesForUnits(@RequestBody CountStatusesForUnitsRequestDTO request) {
        final var response = sendStatusForUnitsService.count(request);
        return CountStatusesResponseDTO.builder()
            .count(response.getCount())
            .max(response.getMax())
            .build();
    }

    @PostMapping("/caregiver/{careGiverId}")
    public SendStatusResponseDTO sendStatusForCareGiver(@PathVariable String careGiverId,
        @RequestBody SendStatusForCareGiverRequestDTO request) {
        final var response = sendStatusForCareGiverService.send(careGiverId, request);
        return SendStatusResponseDTO.builder()
            .count(response)
            .build();
    }

    @PostMapping("count/caregiver/{careGiverId}")
    public CountStatusesResponseDTO countStatusesForCareGiver(@PathVariable String careGiverId,
        @RequestBody CountStatusesForCareGiverRequestDTO request) {
        final var response = sendStatusForCareGiverService.count(careGiverId, request);
        return CountStatusesResponseDTO.builder()
            .count(response.getCount())
            .max(response.getMax())
            .build();
    }

}
