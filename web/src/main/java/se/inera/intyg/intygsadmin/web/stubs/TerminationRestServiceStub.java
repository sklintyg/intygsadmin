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
import se.inera.intyg.intygsadmin.web.integration.TerminationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Profile("ts-stub")
@Service
public class TerminationRestServiceStub implements TerminationRestService {

    private final List<DataExportResponse> list = new ArrayList<>();

    public TerminationRestServiceStub() {
        final var dataExportEntity1 = new DataExportResponse();
        dataExportEntity1.setTerminationId(UUID.randomUUID());
        dataExportEntity1.setCreated(LocalDateTime.now());
        dataExportEntity1.setStatus("Skapad");
        dataExportEntity1.setCreatorName("Sven Svensson");
        dataExportEntity1.setPhoneNumber("08-11111111111");
        dataExportEntity1.setEmailAddress("sven@svensson.se");
        dataExportEntity1.setOrganizationNumber("123456-4321");
        dataExportEntity1.setPersonId("19121212-1212");
        dataExportEntity1.setHsaId("SE2321000888-ABCD");

        final var dataExportEntity2 = new DataExportResponse();
        dataExportEntity2.setTerminationId(UUID.randomUUID());
        dataExportEntity2.setCreated(LocalDateTime.now().minusMinutes(2L));
        dataExportEntity2.setStatus("Kvitterad");
        dataExportEntity2.setCreatorName("Sten Stensson");
        dataExportEntity2.setPhoneNumber("08-2222222222");
        dataExportEntity2.setEmailAddress("sten@stensson.se");
        dataExportEntity2.setOrganizationNumber("654321-1234");
        dataExportEntity2.setPersonId("19770523-2382");
        dataExportEntity2.setHsaId("SE2321000999-F88999");

        list.add(dataExportEntity1);
        list.add(dataExportEntity2);
    }

    @Override
    public List<DataExportResponse> getDataExports() {
        return list;
    }

    @Override
    public DataExportResponse createDataExport(CreateDataExport createDataExporttDTO) {
        final var dataExportResponse = new DataExportResponse();
        dataExportResponse.setTerminationId(UUID.randomUUID());
        dataExportResponse.setCreated(LocalDateTime.now());
        dataExportResponse.setStatus("Skapad");
        dataExportResponse.setCreatorName(createDataExporttDTO.getCreatorName());
        dataExportResponse.setCreatorHSAId(createDataExporttDTO.getCreatorHSAId());
        dataExportResponse.setHsaId(createDataExporttDTO.getHsaId());
        dataExportResponse.setOrganizationNumber(createDataExporttDTO.getOrganizationNumber());
        dataExportResponse.setPersonId(createDataExporttDTO.getPersonId());
        dataExportResponse.setPhoneNumber(createDataExporttDTO.getPhoneNumber());
        dataExportResponse.setEmailAddress(createDataExporttDTO.getEmailAddress());

        list.add(dataExportResponse);

        return dataExportResponse;
    }

    @Override
    public String eraseDataExport(String terminationId) {
        return "Data borttaget";
    }

}
