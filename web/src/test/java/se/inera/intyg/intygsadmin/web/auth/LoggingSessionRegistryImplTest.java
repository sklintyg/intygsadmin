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
package se.inera.intyg.intygsadmin.web.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.web.service.monitoring.MonitoringLogService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration
class LoggingSessionRegistryImplTest {

    private static final IntygsadminUser IAU = new IntygsadminUser(new UserEntity(UUID.randomUUID(), LocalDateTime.now(),
        "HSA1", "karl Nilsson", IntygsadminRole.FULL),
        AuthenticationMethod.FAKE, null);

    @Mock
    private MonitoringLogService monitoringLogService;

    @InjectMocks
    private LoggingSessionRegistryImpl loggingSessionRegistry;

    @Test
    void registerNewSession() {
        loggingSessionRegistry.registerNewSession("ID1", IAU);
        verify(monitoringLogService, times(1)).logUserLogin(any(String.class), any(AuthenticationMethod.class));
    }

    @Test
    void removeSessionInformation_logout() {

        loggingSessionRegistry.registerNewSession("ID1", IAU);
        verify(monitoringLogService, times(1)).logUserLogin(any(String.class), any(AuthenticationMethod.class));

        loggingSessionRegistry.removeSessionInformation("ID1");
        verify(monitoringLogService, times(1)).logUserLogout(any(String.class), any(AuthenticationMethod.class));
    }

    @Test
    void removeSessionInformation_expired() {

        loggingSessionRegistry.registerNewSession("ID1", IAU);
        verify(monitoringLogService, times(1)).logUserLogin(any(String.class), any(AuthenticationMethod.class));

        loggingSessionRegistry.getSessionInformation("ID1").expireNow();

        loggingSessionRegistry.removeSessionInformation("ID1");
        verify(monitoringLogService, times(1)).logUserSessionExpired(any(String.class), any(AuthenticationMethod.class));
    }
}
