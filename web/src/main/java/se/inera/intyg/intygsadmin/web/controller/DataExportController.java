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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportStatusDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportUpdateDTO;
import se.inera.intyg.intygsadmin.web.service.IntygAvslutService;

@RestController
@RequestMapping("/api/dataExport")
@PreAuthorize("hasRole('FULL')")
@Api(
    value = "Data export service",
    description = "The export service API that is used for exporting and removing all data tied to a Care Provider and Organization."
)
public class DataExportController {

    private final IntygAvslutService intygAvslutService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public DataExportController(IntygAvslutService intygAvslutService) {
        this.intygAvslutService = intygAvslutService;
    }

    /**
     * List a page of data exports.
     * @param pageable
     * @return
     */
    @ApiOperation(value = "List a page of data exports.", notes = "List a page of data exports.")
    @GetMapping
    public ResponseEntity<Page<DataExportDTO>> listDataExports(
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "createdAt")
            Pageable pageable) {
        final Page<DataExportDTO> dataExports = intygAvslutService.getDataExports(pageable);

        return ResponseEntity.ok(dataExports);
    }

    /**
     * Get a list of dataExportStatuses for a data export.
     * @param dataExportId
     * @return
     */
    @ApiOperation(value = "Get a list of dataExportStatuses for a data export.", notes = "Returns a list of statuses for the id the id")
    @GetMapping("/{dataExportId}/status")
    public ResponseEntity<List<DataExportStatusDTO>> listDataExportsStatus(@PathVariable UUID dataExportId) {
        final List<DataExportStatusDTO> dataExportStatuses = intygAvslutService.getDataExportStatuses(dataExportId);

        return ResponseEntity.ok(dataExportStatuses);
    }

    /**
     * Update the information regarding a data export.
     * @param createDataExportDTO
     * @return
     */
    @ApiOperation(value = "Create the data export", notes = "Returns the data export that was saved.")
    @PostMapping
    public ResponseEntity<DataExportDTO> createDataExport(@RequestBody CreateDataExportDTO createDataExportDTO) {
        final DataExportDTO savedDTO = intygAvslutService.createDataExport(createDataExportDTO);

        return ResponseEntity.ok(savedDTO);
    }

    /**
     * Update the information regarding a data export.
     * @param dataExportUpdateDTO
     * @return
     */
    @ApiOperation(value = "Update the information regarding a data export.", notes = "Returns the updated data export with the new information added.")
    @PutMapping("/{dataExportId}")
    public ResponseEntity<DataExportDTO> updateDataExport(@PathVariable UUID dataExportId, @RequestBody DataExportUpdateDTO dataExportUpdateDTO) {
        final DataExportDTO savedDTO = intygAvslutService.updateDataExport(
            dataExportId,
            dataExportUpdateDTO.getRepresentativePersonId(),
            dataExportUpdateDTO.getRepresentativePhoneNumber());

        return ResponseEntity.ok(savedDTO);
    }

    /**
     * Remove the request for a data export.
     * @param dataExportId
     * @return
     */
    @ApiOperation(value = "Remove a created data export.", notes = "Returns success or not.")
    @DeleteMapping("/{dataExportId}")
    public ResponseEntity deleteExportRequest(@PathVariable UUID dataExportId) {
        if(intygAvslutService.deleteExportRequest(dataExportId)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

