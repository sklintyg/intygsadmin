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
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.TRACE_ID_KEY;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.SESSION_ID_KEY;

import java.time.LocalDateTime;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.infra.testcertificate.dto.TestCertificateEraseResult;

@Profile("!it-stub")
@Service
public class ITIntegrationRestServiceImpl implements ITIntegrationRestService {

    private static final Logger LOG = LoggerFactory.getLogger(ITIntegrationRestServiceImpl.class);

    private final RestClient restClient;

    @Value("${intygstjansten.internalapi}")
    private String intygstjanstenUrl;

    @Autowired
    public ITIntegrationRestServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ItIntygInfo getIntygInfo(String intygId) {
        try {
            return restClient
                .get()
                .uri(intygstjanstenUrl + "/internalapi/intygInfo/" + intygId)
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve()
                .body(ItIntygInfo.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
            return null;
        }
    }

    @Override
    public TestCertificateEraseResult eraseTestCertificates(LocalDateTime from, LocalDateTime to) {
        final var eraseJSON = new JSONObject();
        eraseJSON.put("from", from);
        eraseJSON.put("to", to);

        return restClient
            .post()
            .uri(intygstjanstenUrl + "/internalapi/testCertificate/erase")
            .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
            .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
            .contentType(MediaType.APPLICATION_JSON)
            .body(eraseJSON)
            .retrieve()
            .body(TestCertificateEraseResult.class);
    }

    @Override
    public Integer getCertificateCount(String hsaId) {
        try {
            return restClient
                .get()
                .uri(intygstjanstenUrl + "/internalapi/intygInfo/" + hsaId + "/count")
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve()
                .body(Integer.class);
        } catch (RestClientException e) {
            LOG.error("Failure fetching certificate count for private practitioner {}.", hsaId, e);
            return null;
        }
    }
}
