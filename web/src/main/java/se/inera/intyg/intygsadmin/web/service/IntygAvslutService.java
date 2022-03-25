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

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportStatusDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.DataExportUpdateDTO;

public interface IntygAvslutService {

    Page<DataExportDTO> getDataExports(Pageable pageable);

    List<DataExportStatusDTO> getDataExportStatuses(UUID dataExportDTOId);

    //TODO
    boolean deleteUserData(UUID dataExportDTOId);

    DataExportDTO updateDataExport(DataExportUpdateDTO dataExportUpdateDTO);

    DataExportDTO createDataExport(CreateDataExportDTO createDataExportDTO);
}