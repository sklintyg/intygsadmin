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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;

@Profile("it-stub")
@RestController
@RequestMapping("/stub/it/")
public class ITIntegrationStub {

    private Map<String, ItIntygInfo> intygInfoMap = new HashMap<>();

    public ITIntegrationStub() {
        ItIntygInfo intygInfoDTO = new ItIntygInfo();
        intygInfoDTO.setIntygId("f63c813d-a13a-4b4b-965f-419dfe98fffe");

        intygInfoMap.put("f63c813d-a13a-4b4b-965f-419dfe98fffe", intygInfoDTO);
    }

    @GetMapping("/internalapi/intygInfo/{hsaId}")
    public ResponseEntity<ItIntygInfo> getIntygInfo(@PathVariable String intygId) {

        if (!intygInfoMap.containsKey(intygId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(intygInfoMap.get(intygId));
    }
}
