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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.intygsadmin.web.controller.dto.IntegratedUnitDTO;
import se.inera.intyg.intygsadmin.web.service.WCIntegrationService;

@Profile("wc-unit-stub")
@Service
public class WCIntegrationServiceStub implements WCIntegrationService {

    private static final String UNIT_ID_1 = "SE4815162344-1A01";
    private static final String UNIT_ID_2 = "SE4815162344-1A02";
    private static final String UNIT_ID_3 = "SE4815162344-1A03";
    private static final String UNIT_ID_4 = "SE4815162344-1A04";

    private static final Map<String, IntegratedUnitDTO> VALID_UNITS = Map
        .of(UNIT_ID_1, new IntegratedUnitDTO(UNIT_ID_1,
                "enhetsnamn1",
                "VardgivareId1",
                "vargivarenamn1",
                LocalDateTime.now(),
                LocalDateTime.now()),
            UNIT_ID_2, new IntegratedUnitDTO(UNIT_ID_2,
                "enhetsnamn2",
                "VardgivareId2",
                "vargivarenamn2",
                LocalDateTime.now(),
                LocalDateTime.now()),
            UNIT_ID_3, new IntegratedUnitDTO(UNIT_ID_3,
                "enhetsnamn3",
                "VardgivareId3",
                "vargivarenamn3",
                LocalDateTime.now(),
                LocalDateTime.now()),
            UNIT_ID_4, new IntegratedUnitDTO(UNIT_ID_4,
                "enhetsnamn4",
                "VardgivareId5",
                "vargivarenamn5",
                LocalDateTime.now(),
                LocalDateTime.now()));

    @Override
    public IntegratedUnitDTO getIntegratedUnit(String hsaId) {
        if (VALID_UNITS.containsKey(hsaId)) {
            return VALID_UNITS.get(hsaId);
        }
        return null;
    }

    @Override
    public List<IntegratedUnitDTO> getAllIntegratedUnits() {
        return new ArrayList<>(VALID_UNITS.values());
    }

    @Override
    public WcIntygInfo getIntygInfo(String intygId) {

        return null;
    }
}
