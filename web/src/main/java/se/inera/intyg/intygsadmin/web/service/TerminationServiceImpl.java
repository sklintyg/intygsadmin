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
package se.inera.intyg.intygsadmin.web.service;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.UpdateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.TerminationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Service
public class TerminationServiceImpl implements TerminationService {

    private static final Logger LOG = LoggerFactory.getLogger(TerminationServiceImpl.class);
    private static final Collator SORT_SWEDISH = Collator.getInstance(new Locale("sv", "SE"));

    private final UserService userService;
    private final TerminationRestService terminationRestService;

    public TerminationServiceImpl(TerminationRestService terminationRestService, UserService userService) {
        this.terminationRestService = terminationRestService;
        this.userService = userService;
        SORT_SWEDISH.setStrength(Collator.PRIMARY);
    }

    /**
     * Return data exports.
     *
     * @param pageable Page object that contains page number and sort order.
     */
    @Override
    public Page<DataExportResponse> getDataExports(Pageable pageable) {
        final var terminations = terminationRestService.getDataExports();

        final var sortColumn = pageable.getSort().get().findFirst().orElseThrow().getProperty();
        final var direction = pageable.getSort().get().findFirst().orElseThrow().getDirection();
        final var sortedTerminations = sort(terminations, sortColumn, direction);

        final var pageNumber = pageable.getPageNumber();
        final var size = pageable.getPageSize();
        final var offset = pageNumber * size;
        final var lastItem = Math.min(offset + size, terminations.size());
        final var page = sortedTerminations.subList(offset, lastItem);
        LOG.info("Returning page {} containing terminations {} to {} of totally {} terminations.", pageNumber + 1, offset + 1, lastItem,
            terminations.size());

        return new PageImpl<>(page, pageable, terminations.size());
    }

    /**
     * Create a data export as a first step to erase the customers data.
     */
    @Override
    public DataExportResponse createDataExport(CreateDataExportDTO createDataExportDTO) {
        final var intygsadminUser = userService.getActiveUser();

        final var createDataExport = new CreateDataExport();
        createDataExport.setCreatorName(intygsadminUser.getName());
        createDataExport.setCreatorHSAId(intygsadminUser.getEmployeeHsaId());
        createDataExport.setHsaId(createDataExportDTO.getHsaId());
        createDataExport.setPersonId(createDataExportDTO.getPersonId());
        createDataExport.setPhoneNumber(createDataExportDTO.getPhoneNumber());
        createDataExport.setEmailAddress(createDataExportDTO.getEmailAddress());
        createDataExport.setOrganizationNumber(createDataExportDTO.getOrganizationNumber());

        return terminationRestService.createDataExport(createDataExport);
    }

    /**
     * Delete the request and not the actual customer data.
     */
    @Override
    public String eraseDataExport(String terminationId) {
        return terminationRestService.eraseDataExport(terminationId);
    }

    /**
     * Update data export.
     *
     * @param dataExportResponse Update information from update form.
     * @return The updated termination.
     */
    @Override
    public DataExportResponse updateDataExport(DataExportResponse dataExportResponse) {
        final var updateDataExportDTO = new UpdateDataExportDTO();
        updateDataExportDTO.setHsaId(dataExportResponse.getHsaId());
        updateDataExportDTO.setPersonId(dataExportResponse.getPersonId());
        updateDataExportDTO.setEmailAddress(dataExportResponse.getEmailAddress());
        updateDataExportDTO.setPhoneNumber(dataExportResponse.getPhoneNumber());

        return terminationRestService.updateDataExport(dataExportResponse.getTerminationId().toString(), updateDataExportDTO);
    }

    /**
     * Trigger a resend of the kryptokey for the provided termination.
     */
    @Override
    public String resendDataExportKey(String terminationId) {
        return terminationRestService.resendDataExportKey(terminationId);
    }

    /**
     * Sort terminations.
     */
    private List<DataExportResponse> sort(List<DataExportResponse> terminations, String sortColumn, Direction direction) {
        return terminations.stream()
            .sorted(getComparator(sortColumn, direction))
            .collect(Collectors.toList());
    }

    /**
     * Determine what comparator to use.
     */
    private Comparator<DataExportResponse> getComparator(String sortColumn, Direction direction) {
        if ("createdAt".equals(sortColumn)) {
            return Comparator.comparing(getKeyExtractor(sortColumn), getKeyComparator(direction));
        }

        return Comparator.comparing(getKeyExtractor(sortColumn), getKeyComparator(direction))
            .thenComparing(DataExportResponse::getCreated, Comparator.reverseOrder());
    }

    /**
     * Check direction to sort.
     */
    private Comparator<Object> getKeyComparator(Direction direction) {
        return direction == Direction.DESC ? SORT_SWEDISH.reversed() : SORT_SWEDISH;
    }

    /**
     * Get what field to sort on.
     */
    private Function<DataExportResponse, String> getKeyExtractor(String sortColumn) {
        switch (sortColumn) {
            case "creatorName":
                return DataExportResponse::getCreatorName;
            case "status":
                return DataExportResponse::getStatus;
            case "careProviderHsaId":
                return DataExportResponse::getHsaId;
            case "organizationNumber":
                return DataExportResponse::getOrganizationNumber;
            case "representativeEmailAddress":
                return DataExportResponse::getEmailAddress;
            case "representativePhoneNumber":
                return DataExportResponse::getPhoneNumber;
            case "representativePersonId":
                return DataExportResponse::getPersonId;
            case "terminationId":
                return t -> t.getTerminationId().toString();
            default:
                return t -> t.getCreated().toString();
        }
    }
}
