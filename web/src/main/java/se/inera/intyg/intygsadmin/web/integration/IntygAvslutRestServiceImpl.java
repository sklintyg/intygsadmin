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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Profile("!ia-stub")
@Service
public class IntygAvslutRestServiceImpl implements IntygAvslutRestService {

    private static final Logger LOG = LoggerFactory.getLogger(IntygAvslutRestServiceImpl.class);

    private RestTemplate restTemplate;

    @Value("${intygavslut.api}")
    private String intygAvslutUrl;

    public IntygAvslutRestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<DataExportResponse> getDataExports() {
        String url = intygAvslutUrl + "api/v1/terminations";
        try {
            DataExportResponse[] dataExportResponses = restTemplate.getForObject(url, DataExportResponse[].class);
            return Arrays.asList(dataExportResponses);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public DataExportResponse createDataExport(CreateDataExport createDataExport) {
        String url = intygAvslutUrl + "api/v1/terminations";
        try {
            return restTemplate.postForObject(url, createDataExport, DataExportResponse.class);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

}
