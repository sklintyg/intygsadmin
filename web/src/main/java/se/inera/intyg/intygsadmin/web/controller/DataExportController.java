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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.service.TerminationService;

@RestController
@RequestMapping("/api/dataExport")
@PreAuthorize("hasRole('FULL')")
@Api(
    value = "Data export service",
    description = "The export service API that is used for exporting and removing all data tied to a Care Provider and Organization."
)
public class DataExportController {

    private final TerminationService terminationService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public DataExportController(TerminationService terminationService) {
        this.terminationService = terminationService;
    }

    /**
     * List a page of data exports.
     * @param pageable
     * @return
     */
    @ApiOperation(value = "List a page of data exports.", notes = "List a page of data exports.")
    @GetMapping
    public ResponseEntity<Page<DataExportResponse>> listDataExports(
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.DESC)
            Pageable pageable) {

        List<DataExportResponse> dataExports = terminationService.getDataExports();
        switch (pageable.getSort().get().findFirst().get().getProperty()) {
            case "creatorName":
                dataExports.sort((Comparator.comparing(DataExportResponse::getCreatorName)));
                break;
            case "terminationId":
                dataExports.sort((Comparator.comparing(DataExportResponse::getTerminationId)));
                break;
            case "status":
                dataExports.sort((Comparator.comparing(DataExportResponse::getStatus)));
                break;
            case "careProviderHsaId":
                dataExports.sort((Comparator.comparing(DataExportResponse::getHsaId)));
                break;
            case "organizationNumber":
                dataExports.sort((Comparator.comparing(DataExportResponse::getOrganizationNumber)));
                break;
            case "representativePersonId":
                dataExports.sort((Comparator.comparing(DataExportResponse::getPersonId)));
                break;
            case "representativePhoneNumber":
                dataExports.sort((Comparator.comparing(DataExportResponse::getPhoneNumber)));
                break;
            default:
                dataExports.sort((Comparator.comparing(DataExportResponse::getCreated)));
        }

        if (pageable.getSort().get().findFirst().get().isDescending()) {
            Collections.reverse(dataExports);
        }

        Page<DataExportResponse> dataExportResponses = new PageImpl<DataExportResponse>(dataExports);
        return ResponseEntity.ok(dataExportResponses);
    }

    /**
     * Create a data export.
     * @param createDataExportDTO
     * @return
     */
    @ApiOperation(value = "Create the data export", notes = "Returns the data export that was saved.")
    @PostMapping
    public ResponseEntity<DataExportResponse> createDataExport(@RequestBody CreateDataExportDTO createDataExportDTO) {
        final DataExportResponse savedDTO = terminationService.createDataExport(createDataExportDTO);

        return ResponseEntity.ok(savedDTO);
    }

    /**
     * Erase the information regarding a data export.
     * @param terminationId
     * @return
     */
    @ApiOperation(value = "Erase all data tied to a data export", notes = "Returns the status of the export")
    @PostMapping("/{terminationId}/erase")
    public ResponseEntity<String> eraseDataExport(@PathVariable("terminationId") String terminationId) {
        final String status = terminationService.erase(terminationId);

        return ResponseEntity.ok(status);
    }
}

