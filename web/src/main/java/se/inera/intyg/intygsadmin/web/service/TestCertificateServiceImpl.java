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
package se.inera.intyg.intygsadmin.web.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationService;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationService;
import se.inera.intyg.intygsadmin.web.jobs.EraseTestCertificateJob;

@Service
public class TestCertificateServiceImpl implements TestCertificateService {

    private static final Logger LOG = LoggerFactory.getLogger(EraseTestCertificateJob.class);

    @Autowired
    private ITIntegrationService itIntegrationService;

    @Autowired
    private WCIntegrationService wcIntegrationService;

    @Value("${erasetestcertificate.erase.after.day}")
    private int eraseAfterDay;

    /**
     * Erase test certificates based on default configuration
     */
    public void eraseTestCertificates() {
        eraseTestCertificates(eraseAfterDay);
    }

    /**
     * Erase test certificates that is older than specified number of days.
     * @param eraseAfterDays    Number of days old.
     */
    public void eraseTestCertificates(int eraseAfterDays) {
        final var to = LocalDateTime.now().minusDays(eraseAfterDays);
        eraseTestCertificates(null, to);
    }

    /**
     * Erase test certificates that was created within a specified datetime range.
     * @param from  Created after from datetime
     * @param to    Create before to datetime
     */
    public void eraseTestCertificates(LocalDateTime from, LocalDateTime to) {
        LOG.info("Erase test certificates from {} to {}", from, to);

        try {
            eraseTestCertificatesInWC(from, to);
        } catch (Exception ex) {
            LOG.error("Erase test certificates in Webcert failed!", ex);
        }

        try {
            eraseTestCertificatesInIT(from, to);
        } catch (Exception ex) {
            LOG.error("Erase test certificates in Intygstjanst failed!", ex);
        }
    }

    private void eraseTestCertificatesInWC(LocalDateTime from, LocalDateTime to) {
        final var start = System.currentTimeMillis();
        final var eraseResult = wcIntegrationService.eraseTestCertificates(from, to);
        final var end = System.currentTimeMillis();
        final var duration = end - start;

        LOG.info("Erase test certificates in Webcert executed in {} seconds. Result: {} erased, {} failed.",
            TimeUnit.MILLISECONDS.toSeconds(duration), eraseResult.getErasedCount(), eraseResult.getFailedCount());
    }

    private void eraseTestCertificatesInIT(LocalDateTime from, LocalDateTime to) {
        final var start = System.currentTimeMillis();
        final var eraseResult = itIntegrationService.eraseTestCertificates(from, to);
        final var end = System.currentTimeMillis();
        final var duration = end - start;

        LOG.info("Erase test certificates in Intygstjanst executed in {} seconds. Result: {} erased, {} failed.",
            TimeUnit.MILLISECONDS.toSeconds(duration), eraseResult.getErasedCount(), eraseResult.getFailedCount());
    }
}
