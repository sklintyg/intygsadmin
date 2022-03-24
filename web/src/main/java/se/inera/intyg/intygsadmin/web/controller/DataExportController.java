/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportStatusDTO;
import se.inera.intyg.intygsadmin.web.service.DataExportServiceImpl;

@RestController
@RequestMapping("/api/dataExport")
@PreAuthorize("hasRole('FULL')")
public class DataExportController {

    private final DataExportServiceImpl dataExportService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public DataExportController(DataExportServiceImpl dataExportService) {
        this.dataExportService = dataExportService;
    }

    @GetMapping
    public ResponseEntity<Page<DataExportDTO>> listDataExports(
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "createdAt")
            Pageable pageable) {
        final var dataExports = dataExportService.getDataExports(pageable);

        return ResponseEntity.ok(dataExports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DataExportStatusDTO>> listDataExportsStatus(@PathVariable UUID id) {
        final var dataExportStatuses = dataExportService.getDataExportStatuses(id);

        return ResponseEntity.ok(dataExportStatuses);
    }

    @PutMapping
    public ResponseEntity<DataExportDTO> addDataExport(@RequestBody DataExportDTO dataExportDTO) {
        final var savedDTO = dataExportService.addDataExport(dataExportDTO);

        return ResponseEntity.ok(savedDTO);
    }

    @PostMapping("/{id}")
    public ResponseEntity<DataExportDTO> updateDataExport(@PathVariable UUID id, @RequestBody DataExportDTO dataExportDTO) {
        dataExportDTO.setId(id);
        final var savedDTO = dataExportService.updateDataExport(dataExportDTO);

        return ResponseEntity.ok(savedDTO);
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }*/
}

