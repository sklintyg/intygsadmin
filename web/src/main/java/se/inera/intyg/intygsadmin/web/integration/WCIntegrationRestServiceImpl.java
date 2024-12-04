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
package se.inera.intyg.intygsadmin.web.integration;

import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcHelper.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.intygsadmin.logging.MdcLogConstants.TRACE_ID_KEY;

import java.time.LocalDateTime;
import java.util.List;
import net.minidev.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import se.inera.intyg.infra.integreradeenheter.IntegratedUnitDTO;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.infra.testcertificate.dto.TestCertificateEraseResult;

@Profile("!wc-stub")
@Service
public class WCIntegrationRestServiceImpl implements WCIntegrationRestService {

    private RestClient restClient;

    @Value("${webcert.internalapi}")
    private String webcertUrl;

    @Autowired
    public WCIntegrationRestServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public IntegratedUnitDTO getIntegratedUnit(String hsaId) {
        try {
            return restClient
                .get()
                .uri(webcertUrl + "/internalapi/integratedUnits/" + hsaId)
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve()
                .body(IntegratedUnitDTO.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
            return null;
        }
    }

    @Override
    public List<IntegratedUnitDTO> getAllIntegratedUnits() {
        return restClient
            .get()
            .uri(webcertUrl + "/internalapi/integratedUnits/all")
            .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
            .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });
    }

    @Override
    public WcIntygInfo getIntygInfo(String intygId) {
        try {
            return restClient
                .get()
                .uri(webcertUrl + "/internalapi/intygInfo/" + intygId)
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve()
                .body(WcIntygInfo.class);
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
            .uri(webcertUrl + "/internalapi/testCertificate/erase")
            .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
            .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
            .contentType(MediaType.APPLICATION_JSON)
            .body(eraseJSON)
            .retrieve()
            .body(TestCertificateEraseResult.class);
    }
}
