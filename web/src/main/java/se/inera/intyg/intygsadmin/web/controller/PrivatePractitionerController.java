/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.PrivatePractitionerDTO;
import se.inera.intyg.intygsadmin.web.integration.PPIntegrationRestService;
import se.inera.intyg.intygsadmin.web.service.PrivatePractitionerService;

@RestController
@RequestMapping("/api/privatepractitioner")
public class PrivatePractitionerController {

    private final PPIntegrationRestService ppIntegrationRestService;
    private final PrivatePractitionerService privatePractitionerService;

    public PrivatePractitionerController(PPIntegrationRestService ppIntegrationRestService,
        PrivatePractitionerService privatePractitionerService) {
        this.ppIntegrationRestService = ppIntegrationRestService;
        this.privatePractitionerService = privatePractitionerService;
    }

    @GetMapping("/{personOrHsaId}")
    public ResponseEntity<PrivatePractitionerDTO> getPrivatePractitioner(@PathVariable String personOrHsaId) {
        final var privatePractitonerDTO = privatePractitionerService.getPrivatePractitioner(personOrHsaId);
        return privatePractitonerDTO != null ? ResponseEntity.ok(privatePractitonerDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getPrivatePractitionerFile() {
        try {
            final var privatePractitionerFile = privatePractitionerService.getPrivatePractitionerFile();

            if (privatePractitionerFile == null) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(getHttpHeaders())
                .body(privatePractitionerFile);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{hsaId}")
    public ResponseEntity<String> unregisterPrivatePractitioner(@PathVariable String hsaId) {
        ppIntegrationRestService.unregisterPrivatePractitioner(hsaId);
        return ResponseEntity.ok().build();
    }

    private HttpHeaders getHttpHeaders() {
        final var header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=privatlakare.xlsx");
        return header;
    }

}
