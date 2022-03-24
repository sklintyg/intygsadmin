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
package se.inera.intyg.intygsadmin.web.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.enums.DataExportStatus;
import se.inera.intyg.intygsadmin.persistence.service.DataExportPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportStatusDTO;
import se.inera.intyg.intygsadmin.web.mapper.DataExportMapper;

@Service
public class DataExportServiceImpl implements DataExportService {

    private final DataExportPersistenceService dataExportPersistenceService;
    private final DataExportMapper dataExportMapper;
    private final UserService userService;

    public DataExportServiceImpl(DataExportPersistenceService dataExportPersistenceService, DataExportMapper dataExportMapper,
        UserService userService) {
        this.dataExportPersistenceService = dataExportPersistenceService;
        this.dataExportMapper = dataExportMapper;
        this.userService = userService;
    }

    @Override
    public Page<DataExportDTO> getDataExports(Pageable pageable) {
        final var dataExportEntities = dataExportPersistenceService.findAll(pageable);

        final var mapDataExports = dataExportMapper.toListDTO(dataExportEntities.getContent());

        return new PageImpl<>(mapDataExports, pageable, dataExportEntities.getTotalElements());
    }

    @Override
    public List<DataExportStatusDTO> getDataExportStatuses(UUID id) {
        final var statuses = new ArrayList<DataExportStatusDTO>();
        statuses.add(new DataExportStatusDTO(LocalDateTime.now().minusDays(1L), DataExportStatus.CREATED.description()));
        statuses.add(new DataExportStatusDTO(LocalDateTime.now(), DataExportStatus.CREATING_PACKAGE.description()));
        return statuses;
    }

    /*public void deleteUser(UUID id) {
        if (getActiveUser().getId().equals(id)) {
            throw new IaServiceException(IaErrorCode.BAD_STATE);
        }

        userPersistenceService.delete(id);
    }*/

    @Override
    public DataExportDTO updateDataExport(DataExportDTO dataExportDTO) {

        // TODO Add validation logic
        // validateDataExport(dataExportDTO);

        final var upsertedDataExport = dataExportPersistenceService.update(dataExportMapper.toEntity(dataExportDTO));
        return dataExportMapper.toDTO(upsertedDataExport);
    }

    @Override
    public DataExportDTO addDataExport(DataExportDTO dataExportDTO) {
        dataExportDTO.setId(null);
        dataExportDTO.setCreatedAt(LocalDateTime.now());
        dataExportDTO.setStatus(DataExportStatus.CREATED);
        dataExportDTO.setAdministratorName(userService.getActiveUser().getName());

        // TODO Add validation logic.
        // validateDataExport(dataExportDTO);

        final var upsertedDataExport = dataExportPersistenceService.add(dataExportMapper.toEntity(dataExportDTO));

        return dataExportMapper.toDTO(upsertedDataExport);
    }

}
