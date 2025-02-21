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

import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.infra.integreradeenheter.IntegratedUnitDTO;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.infra.testcertificate.dto.TestCertificateEraseResult;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForCertificatesIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesForUnitsIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.CountStatusesIntegrationResponseDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationResponseDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCareGiverIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForCertificatesIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusForUnitsIntegrationRequestDTO;
import se.inera.intyg.intygsadmin.web.integration.dto.SendStatusIntegrationRequestDTO;

public interface WCIntegrationRestService {

    IntegratedUnitDTO getIntegratedUnit(String hsaId);

    List<IntegratedUnitDTO> getAllIntegratedUnits();

    WcIntygInfo getIntygInfo(String intygId);

    /**
     * Service for erasing of test certificates.
     *
     * @param from Created after from datetime
     * @param to Create before to datetime
     * @return Result of the erase operation
     */
    TestCertificateEraseResult eraseTestCertificates(LocalDateTime from, LocalDateTime to);

    SendStatusIntegrationResponseDTO sendStatus(SendStatusIntegrationRequestDTO request);

    SendStatusIntegrationResponseDTO sendStatusForCertificates(SendStatusForCertificatesIntegrationRequestDTO request);

    CountStatusesIntegrationResponseDTO countStatusesForCertificates(CountStatusesForCertificatesIntegrationRequestDTO request);

    SendStatusIntegrationResponseDTO sendStatusForUnits(SendStatusForUnitsIntegrationRequestDTO request);

    CountStatusesIntegrationResponseDTO countStatusesForUnits(CountStatusesForUnitsIntegrationRequestDTO request);

    SendStatusIntegrationResponseDTO sendStatusForCareGiver(SendStatusForCareGiverIntegrationRequestDTO request);

    CountStatusesIntegrationResponseDTO countStatusesForCareGiver(CountStatusesForCareGiverIntegrationRequestDTO request);
}
