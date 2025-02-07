/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.integreradeenheter.IntegratedUnitDTO;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent.Source;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEventType;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.infra.testcertificate.dto.TestCertificateEraseResult;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCertificatesIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForUnitsIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationResponseDTO;

@Profile("wc-stub")
@Service
public class WCIntegrationRestServiceStub implements WCIntegrationRestService {

    private static final String UNIT_ID_1 = "SE4815162344-1A01";
    private static final String UNIT_ID_2 = "SE4815162344-1A02";
    private static final String UNIT_ID_3 = "SE4815162344-1A03";
    private static final String UNIT_ID_4 = "SE4815162344-1A04";

    public static final String INTYG_ID_1 = "1edf4f03-0c48-4fe8-9a64-64946aae212a";
    private static final String INTYG_ID_2 = "9afbe083-4da3-41e6-a2a6-43a8dee9d5a4";

    private Map<String, IntegratedUnitDTO> validUnit = new HashMap<>();
    private Map<String, WcIntygInfo> intygInfoMap = new HashMap<>();

    public WCIntegrationRestServiceStub() {
        addUnit(UNIT_ID_1);
        addUnit(UNIT_ID_2);
        addUnit(UNIT_ID_3);
        addUnit(UNIT_ID_4);

        addIntyg(INTYG_ID_1);
        addIntyg(INTYG_ID_2, INTYG_ID_1); // Only WC
    }

    @Override
    public IntegratedUnitDTO getIntegratedUnit(String hsaId) {
        if (validUnit.containsKey(hsaId)) {
            return validUnit.get(hsaId);
        }
        return null;
    }

    @Override
    public List<IntegratedUnitDTO> getAllIntegratedUnits() {
        return new ArrayList<>(validUnit.values());
    }

    @Override
    public WcIntygInfo getIntygInfo(String intygId) {
        if (intygInfoMap.containsKey(intygId)) {
            return intygInfoMap.get(intygId);
        }

        return null;
    }

    @Override
    public TestCertificateEraseResult eraseTestCertificates(LocalDateTime from, LocalDateTime to) {
        return TestCertificateEraseResult.create(0, 0);
    }

    @Override
    public SendStatusIntegrationResponseDTO sendStatus(SendStatusIntegrationRequestDTO request) {
        return SendStatusIntegrationResponseDTO.builder().build();
    }

    @Override
    public SendStatusIntegrationResponseDTO sendStatusForCertificates(SendStatusForCertificatesIntegrationRequestDTO request) {
        return SendStatusIntegrationResponseDTO.builder().build();
    }

    @Override
    public SendStatusIntegrationResponseDTO sendStatusForUnits(SendStatusForUnitsIntegrationRequestDTO request) {
        return SendStatusIntegrationResponseDTO.builder().build();
    }

    @Override
    public SendStatusIntegrationResponseDTO sendStatusForCareGiver(SendStatusForCareGiverIntegrationRequestDTO request) {
        return SendStatusIntegrationResponseDTO.builder().build();
    }

    private void addUnit(String unitId) {
        int number = validUnit.size() + 1;

        IntegratedUnitDTO enhet = new IntegratedUnitDTO(unitId,
            "enhetsnamn" + number,
            "VardgivareId" + number,
            "vargivarenamn" + number,
            LocalDateTime.now(),
            LocalDateTime.now());

        validUnit.put(unitId, enhet);
    }

    private void addIntyg(String intygId) {
        addIntyg(intygId, null);
    }

    private void addIntyg(String intygId, String intygId2) {
        LocalDateTime date = LocalDateTime.now();

        WcIntygInfo intygInfo = new WcIntygInfo();
        intygInfo.setCreatedInWC(true);
        intygInfo.setIntygId(intygId);
        intygInfo.setIntygType("lisjp");
        intygInfo.setIntygVersion("1.0");
        intygInfo.setDraftCreated(date);
        intygInfo.setSignedDate(date.plusHours(1));
        intygInfo.setSentToRecipient(date.plusHours(1));
        intygInfo.setCareGiverHsaId("vg1-id");
        intygInfo.setCareGiverName("vg1");
        intygInfo.setCareUnitName("ve1");
        intygInfo.setCareUnitHsaId("ve1-id");
        intygInfo.setSignedByName("name");
        intygInfo.setSignedByHsaId("hsaId");
        intygInfo.setTestCertificate(false);

        if (!Objects.isNull(intygId2)) {
            intygInfo.getEvents().add(createEvent(date, IntygInfoEventType.IS019, "intygsId", intygId2));
        }

        intygInfo.getEvents().add(createEvent(date, IntygInfoEventType.IS001, "hsaId", "created-by-id", "name", "created-by-name"));
        intygInfo.getEvents().add(createEvent(date.plusMinutes(20), IntygInfoEventType.IS018));
        intygInfo.getEvents().add(createEvent(date.plusHours(1), IntygInfoEventType.IS004, "hsaId", "hsaId", "name", "name"));
        intygInfo.getEvents().add(createEvent(date.plusHours(1), IntygInfoEventType.IS006, "intygsmottagare", "FK"));

        intygInfoMap.put(intygInfo.getIntygId(), intygInfo);
    }

    private IntygInfoEvent createEvent(LocalDateTime date, IntygInfoEventType type) {
        return createEvent(date, type, null, null);
    }

    private IntygInfoEvent createEvent(LocalDateTime date, IntygInfoEventType type, String key1, String data1) {
        return createEvent(date, type, key1, data1, null, null);
    }

    private IntygInfoEvent createEvent(LocalDateTime date, IntygInfoEventType type, String key1, String data1, String key2, String data2) {
        IntygInfoEvent event = new IntygInfoEvent(Source.WEBCERT, date, type);

        if (!Objects.isNull(key1)) {
            event.addData(key1, data1);
        }

        if (!Objects.isNull(key2)) {
            event.addData(key2, data2);
        }
        return event;
    }
}
