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
package se.inera.intyg.intygsadmin.web.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.infra.testcertificate.dto.TestCertificateEraseResult;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationRestService;

@ExtendWith(MockitoExtension.class)
public class TestCertificateServiceTestImpl {

    @Mock
    private ITIntegrationRestService itIntegrationRestService;

    @Mock
    private WCIntegrationRestService wcIntegrationRestService;

    @InjectMocks
    private TestCertificateServiceImpl testCertificateService;

    @Test
    public void testEraseTestCertificates() {
        doReturn(TestCertificateEraseResult.create(1, 0)).when(itIntegrationRestService).eraseTestCertificates(any(), any());
        doReturn(TestCertificateEraseResult.create(2, 0)).when(wcIntegrationRestService).eraseTestCertificates(any(), any());

        testCertificateService.eraseTestCertificates();

        verify(itIntegrationRestService, times(1)).eraseTestCertificates(any(), any());
        verify(wcIntegrationRestService, times(1)).eraseTestCertificates(any(), any());
    }

    @Test
    public void testEraseTestCertificatesFailedWCSuccessIT() {
        doThrow(new RuntimeException()).when(wcIntegrationRestService).eraseTestCertificates(any(), any());
        doReturn(TestCertificateEraseResult.create(1, 0)).when(itIntegrationRestService).eraseTestCertificates(any(), any());

        testCertificateService.eraseTestCertificates();

        verify(itIntegrationRestService, times(1)).eraseTestCertificates(any(), any());
        verify(wcIntegrationRestService, times(1)).eraseTestCertificates(any(), any());
    }

    @Test
    public void testEraseTestCertificatesFailedITSuccessWC() {
        doThrow(new RuntimeException()).when(itIntegrationRestService).eraseTestCertificates(any(), any());
        doReturn(TestCertificateEraseResult.create(2, 0)).when(wcIntegrationRestService).eraseTestCertificates(any(), any());

        testCertificateService.eraseTestCertificates();

        verify(itIntegrationRestService, times(1)).eraseTestCertificates(any(), any());
        verify(wcIntegrationRestService, times(1)).eraseTestCertificates(any(), any());
    }
}
