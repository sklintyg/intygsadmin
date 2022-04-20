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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.IntygAvslutRestService;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Service
public class IntygAvslutServiceImpl implements IntygAvslutService {

    private UserService userService;

    private IntygAvslutRestService intygAvslutRestService;

    private static final Logger LOG = LoggerFactory.getLogger(IntygAvslutServiceImpl.class);

    public IntygAvslutServiceImpl(IntygAvslutRestService intygAvslutRestService, UserService userService) {
        this.intygAvslutRestService = intygAvslutRestService;
        this.userService = userService;
    }

    @Override
    public List<DataExportResponse> getDataExports() {
        //TODO add validation;
        return intygAvslutRestService.getDataExports();
    }

    @Override
    public DataExportResponse createDataExport(CreateDataExportDTO createDataExportDTO) {
        IntygsadminUser intygsadminUser = userService.getActiveUser();

        CreateDataExport createDataExport = new CreateDataExport();
        createDataExport.setCreatorName(intygsadminUser.getName());
        createDataExport.setCreatorHSAId(intygsadminUser.getEmployeeHsaId());
        createDataExport.setHsaId(createDataExportDTO.getHsaId());
        createDataExport.setPersonId(createDataExportDTO.getPersonId());
        createDataExport.setPhoneNumber(createDataExportDTO.getPhoneNumber());
        createDataExport.setOrganizationNumber(createDataExportDTO.getOrganizationNumber());

        //TODO add validation;
        return intygAvslutRestService.createDataExport(createDataExport);
    }
}
