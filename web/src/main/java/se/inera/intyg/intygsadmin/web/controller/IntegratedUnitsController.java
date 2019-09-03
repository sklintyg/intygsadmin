/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.IntegratedUnitDTO;
import se.inera.intyg.intygsadmin.web.service.WCIntegratedUnitsService;
import se.inera.intyg.intygsadmin.web.util.IntegratedUnitsFileWriter;

@RestController
@RequestMapping("/api/integratedUnits")
public class IntegratedUnitsController {

    private WCIntegratedUnitsService wcIntegratedUnitsService;

    public IntegratedUnitsController(WCIntegratedUnitsService wcIntegratedUnitsService) {
        this.wcIntegratedUnitsService = wcIntegratedUnitsService;
    }

    @GetMapping("/{hsaId}")
    public ResponseEntity<IntegratedUnitDTO> getIntegratedUnit(@PathVariable String hsaId) {

        IntegratedUnitDTO integratedUnitDTO = wcIntegratedUnitsService.getIntegratedUnit(hsaId);
        if (integratedUnitDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(integratedUnitDTO);
    }

    @GetMapping("/file")
    public ResponseEntity getIntegratedUnitsFile() {

        List<IntegratedUnitDTO> integratedUnitDTOList = wcIntegratedUnitsService.getAllIntegratedUnits();
        if (integratedUnitDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new IntegratedUnitsFileWriter().writeExcel(integratedUnitDTOList);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=integrerade_enheter.xlsx");

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .headers(header)
            .body(byteArrayOutputStream.toByteArray());
    }

}
    