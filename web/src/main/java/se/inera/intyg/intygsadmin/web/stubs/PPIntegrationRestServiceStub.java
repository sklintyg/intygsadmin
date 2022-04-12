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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.integration.PPIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.PrivatePractitioner;

@Profile("pp-stub")
@Service
public class PPIntegrationRestServiceStub implements PPIntegrationRestService {

    private Map<String, PrivatePractitioner> ppHsaList = new HashMap<>();
    private Map<String, PrivatePractitioner> ppPnrList = new HashMap<>();

    public PPIntegrationRestServiceStub() {
        for (int i = 0; i < 5; i++) {
            var hsaId = "SE123456-X" + i;
            var pnr = "19121212121" + i; // Will not validate, mbut OK in this context
            var privatePractitioner = new PrivatePractitioner(hsaId, "Förnamn" + i + " Efternamn" + i, "Bolag" + i,
                "mail" + i + "@example.com", LocalDateTime
                .now().minusDays(i));
            ppHsaList.put(hsaId, privatePractitioner);
            ppPnrList.put(pnr, privatePractitioner);
        }
    }

    @Override
    public PrivatePractitioner getPrivatePractitioner(String personOrHsaId) {
        if (ppHsaList.containsKey(personOrHsaId)) {
            return ppHsaList.get(personOrHsaId);
        } else if (ppPnrList.containsKey(personOrHsaId)) {
            return ppPnrList.get(personOrHsaId);
        }
        return null;
    }

    @Override
    public List<PrivatePractitioner> getAllPrivatePractitioners() {
        return new ArrayList<>(ppHsaList.values());
    }
}
