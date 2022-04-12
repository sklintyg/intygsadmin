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
package se.inera.intyg.intygsadmin.web.stubs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.enums.DataExportStatus;
import se.inera.intyg.intygsadmin.web.integration.IntygAvslutRestService;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Profile("ia-stub")
@Service
public class IntygAvslutRestServiceStub implements IntygAvslutRestService {

    private List list;

    public IntygAvslutRestServiceStub() {
        list = new ArrayList<DataExportResponse>();

        DataExportResponse dataExportEntity1 = new DataExportResponse();
        dataExportEntity1.setTerminationId(UUID.randomUUID());
        dataExportEntity1.setCreated(LocalDateTime.now());
        dataExportEntity1.setStatus(DataExportStatus.CREATED);
        dataExportEntity1.setCreatorName("Sven Svensson");
        dataExportEntity1.setCreated(LocalDateTime.now());
        dataExportEntity1.setPhoneNumber("08-11111111111");
        dataExportEntity1.setOrganizationNumber("11111111111111111111111111111111");
        dataExportEntity1.setPersonId("0123456789");
        dataExportEntity1.setHsaId("99999999999999999999");

        DataExportResponse dataExportEntity2 = new DataExportResponse();
        dataExportEntity2.setTerminationId(UUID.randomUUID());
        dataExportEntity2.setCreated(LocalDateTime.now());
        dataExportEntity2.setStatus(DataExportStatus.CREATED);
        dataExportEntity2.setCreatorName("Sten stensson");
        dataExportEntity2.setCreated(LocalDateTime.now());
        dataExportEntity2.setPhoneNumber("08-2222222222");
        dataExportEntity2.setOrganizationNumber("22222222222222222222222222222222");
        dataExportEntity2.setPersonId("9876543210");
        dataExportEntity2.setHsaId("888888888888888888888");

        list.add(dataExportEntity1);
        list.add(dataExportEntity2);

    }

    @Override
    public List<DataExportResponse> getDataExports() {
        return list;
    }

    @Override
    public DataExportResponse createDataExport(CreateDataExport createDataExpocreateDataExporttDTO) {
        DataExportResponse dataExportResponse = new DataExportResponse();

        dataExportResponse.setTerminationId(UUID.randomUUID());
        dataExportResponse.setCreated(LocalDateTime.now());
        dataExportResponse.setStatus(DataExportStatus.CREATED);

        dataExportResponse.setCreatorName(createDataExpocreateDataExporttDTO.getCreatorName());
        dataExportResponse.setCreatorHSAId(createDataExpocreateDataExporttDTO.getCreatorHSAId());
        dataExportResponse.setHsaId(createDataExpocreateDataExporttDTO.getHsaId());
        dataExportResponse.setOrganizationNumber(createDataExpocreateDataExporttDTO.getOrganizationalNumber());
        dataExportResponse.setPersonId(createDataExpocreateDataExporttDTO.getPersonId());
        dataExportResponse.setPhoneNumber(createDataExpocreateDataExporttDTO.getPhoneNumber());

        list.add(dataExportResponse);

        return dataExportResponse;
    }

}
