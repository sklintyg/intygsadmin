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
package se.inera.intyg.intygsadmin.web.integration;

import java.util.List;
import se.inera.intyg.intygsadmin.web.integration.model.PrivatePractitioner;

public interface PPIntegrationRestService {

    /**
     * Searches Privatlakarportalen for a given PrivatePractitioner
     *
     * @param personOrHsaId PNR or HSAid
     * @return PrivatePractioner or <code>null</code> when not found
     */
    PrivatePractitioner getPrivatePractitioner(String personOrHsaId);

    /**
     * Get a list of all registered Private Practitioners in Privatlakarportalen
     *
     * @return A list of all PrivatePractitioners from Privatlakarportalen or empty list
     */
    List<PrivatePractitioner> getAllPrivatePractitioners();

    /**
     * Unregister private practitioner.
     *
     * @param hsaId Private practitioner HSA-ID.
     */
    void unregisterPrivatePractitioner(String hsaId);

}
