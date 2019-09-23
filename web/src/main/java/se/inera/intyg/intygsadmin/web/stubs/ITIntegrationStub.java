/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationService;

@Profile("it-stub")
@Service
public class ITIntegrationStub implements ITIntegrationService {

    private Map<String, ItIntygInfo> intygInfoMap = new HashMap<>();

    public ITIntegrationStub() {
        ItIntygInfo intygInfoDTO = new ItIntygInfo();
        intygInfoDTO.setIntygId("f63c813d-a13a-4b4b-965f-419dfe98fffe");

        intygInfoMap.put("f63c813d-a13a-4b4b-965f-419dfe98fffe", intygInfoDTO);
    }

    @Override
    public ItIntygInfo getIntygInfo(@PathVariable String intygId) {

        if (!intygInfoMap.containsKey(intygId)) {
            return null;
        }

        return intygInfoMap.get(intygId);
    }
}
