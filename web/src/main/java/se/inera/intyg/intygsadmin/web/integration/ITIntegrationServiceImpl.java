/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import java.time.LocalDateTime;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.infra.testcertificate.dto.TestCertificateEraseResult;

@Profile("!it-stub")
@Service
public class ITIntegrationServiceImpl implements ITIntegrationService {

    private RestTemplate restTemplate;

    @Value("${intygstjansten.internalapi}")
    private String intygstjanstenUrl;

    @Autowired
    public ITIntegrationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ItIntygInfo getIntygInfo(String intygId) {
        String url = intygstjanstenUrl + "/internalapi/intygInfo/" + intygId;
        try {
            return restTemplate.getForObject(url, ItIntygInfo.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
            return null;
        }
    }

    @Override
    public TestCertificateEraseResult eraseTestCertificates(LocalDateTime from, LocalDateTime to) {
        final var eraseUrl = intygstjanstenUrl + "/internalapi/testCertificate/erase";

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final var eraseJSON = new JSONObject();
        eraseJSON.put("from", from);
        eraseJSON.put("to", to);

        final var eraseRequest = new HttpEntity<>(eraseJSON, headers);
        return restTemplate.postForObject(eraseUrl, eraseRequest, TestCertificateEraseResult.class);
    }
}
