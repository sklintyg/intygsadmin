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
package se.inera.intyg.intygsadmin.web.integration;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.intygsadmin.web.integration.model.PrivatePractitioner;

@Profile("!pp-stub")
@Service
public class PPIntegrationRestRestServiceImpl implements PPIntegrationRestService {

    private RestTemplate restTemplate;

    @Value("${privatlakarportal.internalapi}")
    private String privatlakarportalUrl;

    @Autowired
    public PPIntegrationRestRestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PrivatePractitioner getPrivatePractitioner(String personOrHsaId) {
        String url = privatlakarportalUrl + "/internalapi/privatepractitioner?personOrHsaId=" + personOrHsaId;
        try {
            return restTemplate.getForObject(url, PrivatePractitioner.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
            return null;
        }
    }

    @Override
    public List<PrivatePractitioner> getAllPrivatePractitioners() {
        String url = privatlakarportalUrl + "/internalapi/privatepractitioner/all";

        PrivatePractitioner[] privatePractitionerArray = restTemplate.getForObject(url, PrivatePractitioner[].class);

        if (privatePractitionerArray == null) {
            return List.of();
        }
        return List.of(privatePractitionerArray);
    }
}
