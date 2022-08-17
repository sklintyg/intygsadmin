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
        dataExportEntity2.setCreated(LocalDateTime.now().minusMinutes(1L));
        dataExportEntity2.setStatus("Hämtar intyg");
        dataExportEntity2.setCreatorName("Sten Stensson");
        dataExportEntity2.setPhoneNumber("08-2222222222");
        dataExportEntity2.setEmailAddress("sten@stensson.se");
        dataExportEntity2.setOrganizationNumber("654321-1234");
        dataExportEntity2.setPersonId("19770523-2382");
        dataExportEntity2.setHsaId("SE2321000999-F88999");

        final var dataExportEntity3 = new DataExportResponse();
        dataExportEntity3.setTerminationId(UUID.randomUUID());
        dataExportEntity3.setCreated(LocalDateTime.now().minusMinutes(2L));
        dataExportEntity3.setStatus("Intyg hämtade");
        dataExportEntity3.setCreatorName("Sten Stensson");
        dataExportEntity3.setPhoneNumber("08-33333333");
        dataExportEntity3.setEmailAddress("sten@stensson.se");
        dataExportEntity3.setOrganizationNumber("654321-1111");
        dataExportEntity3.setPersonId("19770523-0001");
        dataExportEntity3.setHsaId("SE2321000999-esc");

        final var dataExportEntity4 = new DataExportResponse();
        dataExportEntity4.setTerminationId(UUID.randomUUID());
        dataExportEntity4.setCreated(LocalDateTime.now().minusMinutes(3L));
        dataExportEntity4.setStatus("Intygstexter hämtade");
        dataExportEntity4.setCreatorName("Sten Stensson");
        dataExportEntity4.setPhoneNumber("08-33333333");
        dataExportEntity4.setEmailAddress("sten@stensson.se");
        dataExportEntity4.setOrganizationNumber("654321-1111");
        dataExportEntity4.setPersonId("19770523-0001");
        dataExportEntity4.setHsaId("SE2321000999-esc");

        final var dataExportEntity5 = new DataExportResponse();
        dataExportEntity5.setTerminationId(UUID.randomUUID());
        dataExportEntity5.setCreated(LocalDateTime.now().minusMinutes(4L));
        dataExportEntity5.setStatus("Uppladdat");
        dataExportEntity5.setCreatorName("Sten Stensson");
        dataExportEntity5.setPhoneNumber("08-33333333");
        dataExportEntity5.setEmailAddress("sten@stensson.se");
        dataExportEntity5.setOrganizationNumber("654321-1111");
        dataExportEntity5.setPersonId("19770523-0001");
        dataExportEntity5.setHsaId("SE2321000999-esc");

        final var dataExportEntity6 = new DataExportResponse();
        dataExportEntity6.setTerminationId(UUID.randomUUID());
        dataExportEntity6.setCreated(LocalDateTime.now().minusMinutes(5L));
        dataExportEntity6.setStatus("Notifiering skickad");
        dataExportEntity6.setCreatorName("Sten Stensson");
        dataExportEntity6.setPhoneNumber("08-33333333");
        dataExportEntity6.setEmailAddress("sten@stensson.se");
        dataExportEntity6.setOrganizationNumber("654321-1111");
        dataExportEntity6.setPersonId("19770523-0001");
        dataExportEntity6.setHsaId("SE2321000999-esc");

        final var dataExportEntity7 = new DataExportResponse();
        dataExportEntity7.setTerminationId(UUID.randomUUID());
        dataExportEntity7.setCreated(LocalDateTime.now().minusMinutes(6L));
        dataExportEntity7.setStatus("Påminnelse skickad");
        dataExportEntity7.setCreatorName("Sten Stensson");
        dataExportEntity7.setPhoneNumber("08-33333333");
        dataExportEntity7.setEmailAddress("sten@stensson.se");
        dataExportEntity7.setOrganizationNumber("654321-1111");
        dataExportEntity7.setPersonId("19770523-0001");
        dataExportEntity7.setHsaId("SE2321000999-esc");

        final var dataExportEntity8 = new DataExportResponse();
        dataExportEntity8.setTerminationId(UUID.randomUUID());
        dataExportEntity8.setCreated(LocalDateTime.now().minusMinutes(7L));
        dataExportEntity8.setStatus("Kvitterad");
        dataExportEntity8.setCreatorName("Sten Stensson");
        dataExportEntity8.setPhoneNumber("08-33333333");
        dataExportEntity8.setEmailAddress("sten@stensson.se");
        dataExportEntity8.setOrganizationNumber("654321-1111");
        dataExportEntity8.setPersonId("19770523-0001");
        dataExportEntity8.setHsaId("SE2321000999-esc");

        final var dataExportEntity9 = new DataExportResponse();
        dataExportEntity9.setTerminationId(UUID.randomUUID());
        dataExportEntity9.setCreated(LocalDateTime.now().minusMinutes(8L));
        dataExportEntity9.setStatus("Lösenord skickat");
        dataExportEntity9.setCreatorName("Sten Stensson");
        dataExportEntity9.setPhoneNumber("08-33333333");
        dataExportEntity9.setEmailAddress("sten@stensson.se");
        dataExportEntity9.setOrganizationNumber("654321-1111");
        dataExportEntity9.setPersonId("19770523-0001");
        dataExportEntity9.setHsaId("SE2321000999-esc");

        final var dataExportEntity10 = new DataExportResponse();
        dataExportEntity10.setTerminationId(UUID.randomUUID());
        dataExportEntity10.setCreated(LocalDateTime.now().minusMinutes(9L));
        dataExportEntity10.setStatus("Starta radering");
        dataExportEntity10.setCreatorName("Sten Stensson");
        dataExportEntity10.setPhoneNumber("08-33333333");
        dataExportEntity10.setEmailAddress("sten@stensson.se");
        dataExportEntity10.setOrganizationNumber("654321-1111");
        dataExportEntity10.setPersonId("19770523-0001");
        dataExportEntity10.setHsaId("SE2321000999-esc");

        final var dataExportEntity11 = new DataExportResponse();
        dataExportEntity11.setTerminationId(UUID.randomUUID());
        dataExportEntity11.setCreated(LocalDateTime.now().minusMinutes(10L));
        dataExportEntity11.setStatus("Radering pågår");
        dataExportEntity11.setCreatorName("Sten Stensson");
        dataExportEntity11.setPhoneNumber("08-33333333");
        dataExportEntity11.setEmailAddress("sten@stensson.se");
        dataExportEntity11.setOrganizationNumber("654321-1111");
        dataExportEntity11.setPersonId("19770523-0001");
        dataExportEntity11.setHsaId("SE2321000999-esc");

        final var dataExportEntity12 = new DataExportResponse();
        dataExportEntity12.setTerminationId(UUID.randomUUID());
        dataExportEntity12.setCreated(LocalDateTime.now().minusMinutes(11L));
        dataExportEntity12.setStatus("Radering pågår");
        dataExportEntity12.setCreatorName("Sten Stensson");
        dataExportEntity12.setPhoneNumber("08-33333333");
        dataExportEntity12.setEmailAddress("sten@stensson.se");
        dataExportEntity12.setOrganizationNumber("654321-1111");
        dataExportEntity12.setPersonId("19770523-0001");
        dataExportEntity12.setHsaId("SE2321000999-esc");

        final var dataExportEntity13 = new DataExportResponse();
        dataExportEntity13.setTerminationId(UUID.randomUUID());
        dataExportEntity13.setCreated(LocalDateTime.now().minusMinutes(12L));
        dataExportEntity13.setStatus("Radering avbruten");
        dataExportEntity13.setCreatorName("Sten Stensson");
        dataExportEntity13.setPhoneNumber("08-33333333");
        dataExportEntity13.setEmailAddress("sten@stensson.se");
        dataExportEntity13.setOrganizationNumber("654321-1111");
        dataExportEntity13.setPersonId("19770523-0001");
        dataExportEntity13.setHsaId("SE2321000999-esc");

        final var dataExportEntity14 = new DataExportResponse();
        dataExportEntity14.setTerminationId(UUID.randomUUID());
        dataExportEntity14.setCreated(LocalDateTime.now().minusMinutes(13L));
        dataExportEntity14.setStatus("Radering utförd");
        dataExportEntity14.setCreatorName("Sten Stensson");
        dataExportEntity14.setPhoneNumber("08-33333333");
        dataExportEntity14.setEmailAddress("sten@stensson.se");
        dataExportEntity14.setOrganizationNumber("654321-1111");
        dataExportEntity14.setPersonId("19770523-0001");
        dataExportEntity14.setHsaId("SE2321000999-esc");

        list.add(dataExportEntity1);
        list.add(dataExportEntity2);
        list.add(dataExportEntity3);
        list.add(dataExportEntity4);
        list.add(dataExportEntity5);
        list.add(dataExportEntity6);
        list.add(dataExportEntity7);
        list.add(dataExportEntity8);
        list.add(dataExportEntity9);
        list.add(dataExportEntity10);
        list.add(dataExportEntity11);
        list.add(dataExportEntity12);
        list.add(dataExportEntity13);
        list.add(dataExportEntity14);
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

    @Override
    public String resendDataExportKey(String terminationId) {
        return "Lösenord skickat";
    }

}
