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

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import se.inera.intyg.intygsadmin.web.controller.dto.UpdateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@Profile("!ts-stub")
@Service
public class TerminationRestServiceImpl implements TerminationRestService {

    private static final Logger LOG = LoggerFactory.getLogger(TerminationRestServiceImpl.class);

    private final RestClient restClient;

    @Value("${terminationservice.api}")
    private String terminationServiceUrl;

    public TerminationRestServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Get all data exports.
     *
     * @return List of available data exports.
     */
    @Override
    public List<DataExportResponse> getDataExports() {
        try {
            return restClient
                .get()
                .uri(terminationServiceUrl + "/api/v1/terminations")
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        } catch (RestClientException | NullPointerException e) {
            LOG.error("Failure fetching terminations from cts.", e);
            throw e;
        }
    }

    /**
     * Create a data export.
     *
     * @param createDataExport Information for the creation of a new termination.
     * @return The created data export.
     */
    @Override
    public DataExportResponse createDataExport(CreateDataExport createDataExport) {
        try {
            return restClient
                .post()
                .uri(terminationServiceUrl + "/api/v1/terminations")
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .contentType(MediaType.APPLICATION_JSON)
                .body(createDataExport)
                .retrieve()
                .body(DataExportResponse.class);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Trigger the deletion of all data tied to a data export.
     *
     * @param terminationId Termination id of the termination to be deleted.
     */
    @Override
    public String eraseDataExport(String terminationId) {
        try {
            return restClient
                .post()
                .uri(terminationServiceUrl + "/api/v1/terminations/" + terminationId + "/erase")
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Trigger update of a data export.
     *
     * @param terminationId Id of the termination to be updated.
     * @param updateDataExportDTO Information for the update.
     * @return The updated data export.
     */
    @Override
    public DataExportResponse updateDataExport(String terminationId, UpdateDataExportDTO updateDataExportDTO) {
        try {
            return restClient
                .post()
                .uri(terminationServiceUrl + "/api/v1/terminations/" + terminationId)
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateDataExportDTO)
                .retrieve()
                .body(DataExportResponse.class);
        } catch (RestClientException e) {
            LOG.error("Failure updating termination.", e);
            throw e;
        }
    }

    /**
     * Trigger a resend of the kryptokey for the provided termination.
     *
     * @param terminationId Id of termination for which crypto key should be re-sent.
     */
    @Override
    public String resendDataExportKey(String terminationId) {
        try {
            return restClient
                .post()
                .uri(terminationServiceUrl + "/api/v1/terminations/" + terminationId + "/resendpassword")
                .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
                .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        } catch (RestClientException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }
}
