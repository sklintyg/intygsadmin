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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Profile("!ts-stub")
@Service
public class TerminationRestServiceImpl implements TerminationRestService {

    private static final Logger LOG = LoggerFactory.getLogger(TerminationRestServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${terminationservice.api}")
    private String terminationServiceUrl;

    public TerminationRestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<DataExportResponse> getDataExports() {
        String url = terminationServiceUrl + "api/v1/terminations";
        try {
            DataExportResponse[] dataExportResponses = restTemplate.getForObject(url, DataExportResponse[].class);
            return Arrays.asList(Objects.requireNonNull(dataExportResponses));
        } catch (RestClientException | NullPointerException e) {
            LOG.error("Failure fetching terminations from cts.", e);
            throw e;
        }
    }

    @Override
    public DataExportResponse createDataExport(CreateDataExport createDataExport) {
        String url = terminationServiceUrl + "api/v1/terminations";
        try {
            return restTemplate.postForObject(url, createDataExport, DataExportResponse.class);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public String eraseDataExport(String terminationId) {
        String url = terminationServiceUrl + "api/v1/terminations/" + terminationId + "/erase";
        try {
            return restTemplate.postForObject(url, null, String.class);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }
}
