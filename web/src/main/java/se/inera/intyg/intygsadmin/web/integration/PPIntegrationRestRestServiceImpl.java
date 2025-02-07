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

import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.TRACE_ID_KEY;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import se.inera.intyg.intygsadmin.web.integration.model.PrivatePractitioner;

@Profile("!pp-stub")
@Service
public class PPIntegrationRestRestServiceImpl implements PPIntegrationRestService {

    private static final Logger LOG = LoggerFactory.getLogger(PPIntegrationRestRestServiceImpl.class);

    private final RestClient restClient;

    @Value("${privatlakarportal.internalapi}")
    private String privatlakarportalUrl;

    @Autowired
    public PPIntegrationRestRestServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PrivatePractitioner getPrivatePractitioner(String personOrHsaId) {
        try {
            return restClient
                .get()
                .uri(privatlakarportalUrl + "/internalapi/privatepractitioner?personOrHsaId=" + personOrHsaId)
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve()
                .body(PrivatePractitioner.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
            return null;
        }
    }

    @Override
    public List<PrivatePractitioner> getAllPrivatePractitioners() {
        return restClient
            .get()
            .uri(privatlakarportalUrl + "/internalapi/privatepractitioner/all")
            .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
            .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public void unregisterPrivatePractitioner(String hsaId) {
        try {
            restClient
                .delete()
                .uri(privatlakarportalUrl + "/internalapi/privatepractitioner/erase/" + hsaId)
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve();
        } catch (RestClientException e) {
            LOG.error("Failure unregistering private practitioner {}.", hsaId, e);
            throw e;
        }
    }
}
