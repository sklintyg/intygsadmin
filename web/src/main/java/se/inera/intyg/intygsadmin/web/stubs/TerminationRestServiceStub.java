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
package se.inera.intyg.intygsadmin.web.stubs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.controller.dto.UpdateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.TerminationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Profile("ts-stub")
@Service
public class TerminationRestServiceStub implements TerminationRestService {

    private final List<DataExportResponse> list = new ArrayList<>();

    public TerminationRestServiceStub() {
        createStubData();
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
    public DataExportResponse updateDataExport(String terminationId, UpdateDataExportDTO updateDataExportDTO) {
        final var dataExport = getDataExport(terminationId);

        if (!updateDataExportDTO.getPhoneNumber().equals(dataExport.getPhoneNumber())) {
            dataExport.setPhoneNumber(updateDataExportDTO.getPhoneNumber());

            if (requiresReNotification(dataExport.getStatus())) {
                dataExport.setStatus("Uppladdat");
            }
        }

        if (!updateDataExportDTO.getEmailAddress().equals(dataExport.getEmailAddress())) {
            dataExport.setEmailAddress(updateDataExportDTO.getEmailAddress());

            if (requiresReNotification(dataExport.getStatus())) {
                dataExport.setStatus("Uppladdat");
            }
        }

        if (!updateDataExportDTO.getPersonId().equals((dataExport.getPersonId()))) {
            dataExport.setPersonId(updateDataExportDTO.getPersonId());

            if (requiresPackageReExport(dataExport.getStatus())) {
                dataExport.setStatus("Intygstexter hämtade");
            }
        }

        if (!updateDataExportDTO.getHsaId().equals(dataExport.getHsaId())) {
            dataExport.setHsaId(updateDataExportDTO.getHsaId());
            dataExport.setStatus("Skapad");
        }

        return dataExport;
    }

    @Override
    public String eraseDataExport(String terminationId) {
        final var dataExport = getDataExport(terminationId);
        dataExport.setStatus("Starta radering");
        return "Data borttaget";
    }

    @Override
    public String resendDataExportKey(String terminationId) {
        final var dataExport = getDataExport(terminationId);
        dataExport.setStatus("Kryptonyckel skickad igen");
        return "Lösenord skickat";
    }

    private DataExportResponse getDataExport(String terminationId) {
        return list.stream()
            .filter(item -> item.getTerminationId().toString().equals(terminationId))
            .findFirst().orElseThrow();
    }

    private boolean requiresReNotification(String status) {
        return "Notifiering skickad".equals(status) || "Påminnelse skickad".equals(status);
    }

    private boolean requiresPackageReExport(String status) {
        return "Uppladdat".equals(status) || "Notifiering skickad".equals(status) || "Påminnelse skickad".equals(status);
    }

    private void createStubData() {
        final var dataExportEntity1 = new DataExportResponse();
        dataExportEntity1.setTerminationId(UUID.randomUUID());
        dataExportEntity1.setCreated(LocalDateTime.now());
        dataExportEntity1.setStatus("Skapad");
        dataExportEntity1.setCreatorName("Noah Andersson");
        dataExportEntity1.setPhoneNumber("08-11111111111");
        dataExportEntity1.setEmailAddress("Noah@Andersson.se");
        dataExportEntity1.setOrganizationNumber("123456-1111");
        dataExportEntity1.setPersonId("19121212-1212");
        dataExportEntity1.setHsaId("SE2321000888-1");

        final var dataExportEntity2 = new DataExportResponse();
        dataExportEntity2.setTerminationId(UUID.randomUUID());
        dataExportEntity2.setCreated(LocalDateTime.now().minusMinutes(1L));
        dataExportEntity2.setStatus("Hämtar intyg");
        dataExportEntity2.setCreatorName("William Johansson");
        dataExportEntity2.setPhoneNumber("08-2222222222");
        dataExportEntity2.setEmailAddress("William@Johansson.se");
        dataExportEntity2.setOrganizationNumber("654321-2222");
        dataExportEntity2.setPersonId("19770523-2382");
        dataExportEntity2.setHsaId("SE2321000999-2");

        final var dataExportEntity3 = new DataExportResponse();
        dataExportEntity3.setTerminationId(UUID.randomUUID());
        dataExportEntity3.setCreated(LocalDateTime.now().minusMinutes(2L));
        dataExportEntity3.setStatus("Intyg hämtade");
        dataExportEntity3.setCreatorName("Liam Karlsson");
        dataExportEntity3.setPhoneNumber("08-33333333");
        dataExportEntity3.setEmailAddress("Liam@Karlsson.se");
        dataExportEntity3.setOrganizationNumber("654321-3333");
        dataExportEntity3.setPersonId("19770523-0003");
        dataExportEntity3.setHsaId("SE2321000999-3");

        final var dataExportEntity4 = new DataExportResponse();
        dataExportEntity4.setTerminationId(UUID.randomUUID());
        dataExportEntity4.setCreated(LocalDateTime.now().minusHours(1L).minusMinutes(1L));
        dataExportEntity4.setStatus("Intygstexter hämtade");
        dataExportEntity4.setCreatorName("Hugo Nilsson");
        dataExportEntity4.setPhoneNumber("08-4444444444");
        dataExportEntity4.setEmailAddress("Hugo@Nilsson.se");
        dataExportEntity4.setOrganizationNumber("654321-4444");
        dataExportEntity4.setPersonId("19770523-0004");
        dataExportEntity4.setHsaId("SE2321000999-4");

        final var dataExportEntity5 = new DataExportResponse();
        dataExportEntity5.setTerminationId(UUID.randomUUID());
        dataExportEntity5.setCreated(LocalDateTime.now().minusHours(1L).minusMinutes(2L));
        dataExportEntity5.setStatus("Uppladdat");
        dataExportEntity5.setCreatorName("Lucas Eriksson");
        dataExportEntity5.setPhoneNumber("08-555555555");
        dataExportEntity5.setEmailAddress("Lucas@Eriksson.se");
        dataExportEntity5.setOrganizationNumber("654321-5555");
        dataExportEntity5.setPersonId("19770523-0005");
        dataExportEntity5.setHsaId("SE2321000999-5");

        final var dataExportEntity6 = new DataExportResponse();
        dataExportEntity6.setTerminationId(UUID.randomUUID());
        dataExportEntity6.setCreated(LocalDateTime.now().minusHours(2L).minusMinutes(1L));
        dataExportEntity6.setStatus("Notifiering skickad");
        dataExportEntity6.setCreatorName("Adam Larsson");
        dataExportEntity6.setPhoneNumber("08-6666666666");
        dataExportEntity6.setEmailAddress("Adam@Larsson.se");
        dataExportEntity6.setOrganizationNumber("654321-6666");
        dataExportEntity6.setPersonId("19770523-0006");
        dataExportEntity6.setHsaId("SE2321000999-6");

        final var dataExportEntity7 = new DataExportResponse();
        dataExportEntity7.setTerminationId(UUID.randomUUID());
        dataExportEntity7.setCreated(LocalDateTime.now().minusHours(2L).minusMinutes(2L));
        dataExportEntity7.setStatus("Påminnelse skickad");
        dataExportEntity7.setCreatorName("Alice Olsson");
        dataExportEntity7.setPhoneNumber("08-77777777");
        dataExportEntity7.setEmailAddress("Alice@Olsson.se");
        dataExportEntity7.setOrganizationNumber("654321-7777");
        dataExportEntity7.setPersonId("19770523-0007");
        dataExportEntity7.setHsaId("SE2321000999-7");

        final var dataExportEntity8 = new DataExportResponse();
        dataExportEntity8.setTerminationId(UUID.randomUUID());
        dataExportEntity8.setCreated(LocalDateTime.now().minusDays(1L).minusHours(1L).minusMinutes(1L));
        dataExportEntity8.setStatus("Kvitterad");
        dataExportEntity8.setCreatorName("Maja Persson");
        dataExportEntity8.setPhoneNumber("08-888888888");
        dataExportEntity8.setEmailAddress("Maja@Persson.se");
        dataExportEntity8.setOrganizationNumber("654321-8888");
        dataExportEntity8.setPersonId("19770523-0008");
        dataExportEntity8.setHsaId("SE2321000999-8");

        final var dataExportEntity9 = new DataExportResponse();
        dataExportEntity9.setTerminationId(UUID.randomUUID());
        dataExportEntity9.setCreated(LocalDateTime.now().minusDays(1L).minusHours(1L).minusMinutes(2L));
        dataExportEntity9.setStatus("Kryptonyckel skickad");
        dataExportEntity9.setCreatorName("Vera Svensson");
        dataExportEntity9.setPhoneNumber("08-999999999");
        dataExportEntity9.setEmailAddress("Vera@Svensson.se");
        dataExportEntity9.setOrganizationNumber("654321-9999");
        dataExportEntity9.setPersonId("19770523-0009");
        dataExportEntity9.setHsaId("SE2321000999-9");

        final var dataExportEntity10 = new DataExportResponse();
        dataExportEntity10.setTerminationId(UUID.randomUUID());
        dataExportEntity10.setCreated(LocalDateTime.now().minusDays(1L).minusHours(2L).minusMinutes(2L));
        dataExportEntity10.setStatus("Kryptonyckel skickad igen");
        dataExportEntity10.setCreatorName("Alma Gustafsson");
        dataExportEntity10.setPhoneNumber("08-1000000000");
        dataExportEntity10.setEmailAddress("Alma@Gustafsson.se");
        dataExportEntity10.setOrganizationNumber("654321-1000");
        dataExportEntity10.setPersonId("19770523-0010");
        dataExportEntity10.setHsaId("SE2321000999-10");

        final var dataExportEntity11 = new DataExportResponse();
        dataExportEntity11.setTerminationId(UUID.randomUUID());
        dataExportEntity11.setCreated(LocalDateTime.now().minusDays(2L).minusHours(1L).minusMinutes(1L));
        dataExportEntity11.setStatus("Starta radering");
        dataExportEntity11.setCreatorName("Selma Stensson");
        dataExportEntity11.setPhoneNumber("08-110000000");
        dataExportEntity11.setEmailAddress("Selma@Stensson.se");
        dataExportEntity11.setOrganizationNumber("654321-1100");
        dataExportEntity11.setPersonId("19770523-0011");
        dataExportEntity11.setHsaId("SE2321000999-11");

        final var dataExportEntity12 = new DataExportResponse();
        dataExportEntity12.setTerminationId(UUID.randomUUID());
        dataExportEntity12.setCreated(LocalDateTime.now().minusDays(2L).minusHours(1L).minusMinutes(2L));
        dataExportEntity12.setStatus("Radering pågår");
        dataExportEntity12.setCreatorName("Elsa pettersson");
        dataExportEntity12.setPhoneNumber("08-12000000000");
        dataExportEntity12.setEmailAddress("Elsa@pettersson.se");
        dataExportEntity12.setOrganizationNumber("654321-1200");
        dataExportEntity12.setPersonId("19770523-0012");
        dataExportEntity12.setHsaId("SE2321000999-12");

        final var dataExportEntity13 = new DataExportResponse();
        dataExportEntity13.setTerminationId(UUID.randomUUID());
        dataExportEntity13.setCreated(LocalDateTime.now().minusDays(2L).minusHours(2L).minusMinutes(1L));
        dataExportEntity13.setStatus("Radering avbruten");
        dataExportEntity13.setCreatorName("Lilly Stensson");
        dataExportEntity13.setPhoneNumber("08-1300000000");
        dataExportEntity13.setEmailAddress("Lilly@stensson.se");
        dataExportEntity13.setOrganizationNumber("654321-1300");
        dataExportEntity13.setPersonId("19770523-0013");
        dataExportEntity13.setHsaId("SE2321000999-13");

        final var dataExportEntity14 = new DataExportResponse();
        dataExportEntity14.setTerminationId(UUID.randomUUID());
        dataExportEntity14.setCreated(LocalDateTime.now().minusDays(2L).minusHours(2L).minusMinutes(2L));
        dataExportEntity14.setStatus("Radering utförd");
        dataExportEntity14.setCreatorName("Frans Fransson");
        dataExportEntity14.setPhoneNumber("08-140000000");
        dataExportEntity14.setEmailAddress("Frans@Fransson.se");
        dataExportEntity14.setOrganizationNumber("654321-1400");
        dataExportEntity14.setPersonId("19770523-0014");
        dataExportEntity14.setHsaId("SE2321000999-14");

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
}
