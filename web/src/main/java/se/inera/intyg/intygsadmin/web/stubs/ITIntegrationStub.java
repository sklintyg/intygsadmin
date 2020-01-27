/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent.Source;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEventType;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationService;

@Profile("it-stub")
@Service
public class ITIntegrationStub implements ITIntegrationService {

    private Map<String, ItIntygInfo> intygInfoMap = new HashMap<>();

    public ITIntegrationStub() {
        addIntyg(WCIntegrationServiceStub.INTYG_ID_1);
        addIntyg("f63c813d-a13a-4b4b-965f-419dfe98fffe"); // Only in IT
    }

    @Override
    public ItIntygInfo getIntygInfo(@PathVariable String intygId) {
        if (intygInfoMap.containsKey(intygId)) {
            return intygInfoMap.get(intygId);
        }

        return null;
    }


    private void addIntyg(String intygId) {
        LocalDateTime date = LocalDateTime.now();

        ItIntygInfo intygInfo = new ItIntygInfo();
        intygInfo.setIntygId(intygId);
        intygInfo.setNumberOfRecipients(intygInfoMap.size());
        intygInfo.setIntygType("lisjp");
        intygInfo.setIntygVersion("1.0");
        intygInfo.setSignedDate(date);
        intygInfo.setReceivedDate(date.plusMinutes(1));
        intygInfo.setSentToRecipient(date.plusMinutes(10));
        intygInfo.setCareGiverHsaId("vg1-id");
        intygInfo.setCareGiverName("vg1");
        intygInfo.setCareUnitName("ve1");
        intygInfo.setCareUnitHsaId("ve1-id");
        intygInfo.setSignedByName("name");
        intygInfo.setSignedByHsaId("hsaId");

        intygInfo.getEvents().add(new IntygInfoEvent(Source.INTYGSTJANSTEN, date.plusMinutes(1), IntygInfoEventType.IS005));

        IntygInfoEvent signed = new IntygInfoEvent(Source.INTYGSTJANSTEN, date, IntygInfoEventType.IS004);
        signed.addData("name", "name");
        signed.addData("hsaId", "hsaId");
        intygInfo.getEvents().add(signed);

        intygInfoMap.put(intygInfo.getIntygId(), intygInfo);
    }
}
