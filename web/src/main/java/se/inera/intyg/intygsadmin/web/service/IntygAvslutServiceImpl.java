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
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.enums.DataExportStatus;
import se.inera.intyg.intygsadmin.persistence.service.IntygAvslutPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportStatusDTO;
import se.inera.intyg.intygsadmin.web.mapper.DataExportMapper;

@Service
public class IntygAvslutServiceImpl implements IntygAvslutService {

    private final IntygAvslutPersistenceService intygAvslutPersistenceService;
    private final DataExportMapper dataExportMapper;
    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(IntygAvslutServiceImpl.class);

    public IntygAvslutServiceImpl(IntygAvslutPersistenceService intygAvslutPersistenceService, DataExportMapper dataExportMapper,
        UserService userService) {
        this.intygAvslutPersistenceService = intygAvslutPersistenceService;
        this.dataExportMapper = dataExportMapper;
        this.userService = userService;
    }

    @Override
    public Page<DataExportDTO> getDataExports(Pageable pageable) {
        final var dataExportEntities = intygAvslutPersistenceService.findAll(pageable);

        final var mapDataExports = dataExportMapper.toListDTO(dataExportEntities.getContent());

        return new PageImpl<>(mapDataExports, pageable, dataExportEntities.getTotalElements());
    }

    @Override //TODO Implement
    public List<DataExportStatusDTO> getDataExportStatuses(UUID dataExportDTOId) {
        throw new NotImplementedException("Operation not supported");
    }

    @Override //TODO Implement
    public boolean deleteUserData(UUID dataExportDTOId) {
        throw new NotImplementedException("Operation not supported");
    }

    @Override
    public DataExportDTO updateDataExport(UUID dataExportId, String representativePersonId, String representativePhoneNumber) {

        // TODO Add validation logic
        // validateDataExport(dataExportDTO);

        DataExportDTO dataExportDTO = new DataExportDTO();
        dataExportDTO.setId(dataExportId);
        dataExportDTO.setRepresentativePersonId(representativePersonId);
        dataExportDTO.setRepresentativePhoneNumber(representativePhoneNumber);

        final var updatedDataExport = intygAvslutPersistenceService.update(dataExportMapper.toEntity(dataExportDTO));
        return dataExportMapper.toDTO(updatedDataExport);
    }

    @Override
    public DataExportDTO createDataExport(CreateDataExportDTO createDataExportDTO) {

        DataExportDTO  dataExportDTO = dataExportMapper.toEntity(createDataExportDTO);
        //Default values
        dataExportDTO.setId(null);
        dataExportDTO.setCreatedAt(LocalDateTime.now());
        dataExportDTO.setStatus(DataExportStatus.CREATED);
        dataExportDTO.setAdministratorName(userService.getActiveUser().getName());

        // TODO Add validation logic.
        // validateDataExport(dataExportDTO);

        final var createdDataExport = intygAvslutPersistenceService.add(dataExportMapper.toEntity(dataExportDTO));

        return dataExportMapper.toDTO(createdDataExport);
    }

    @Override
    public boolean deleteExportRequest(UUID dataExportId) {
        throw new NotImplementedException("Operation not supported");
    }

}
