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

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import se.inera.intyg.intygsadmin.web.service.monitoring.MonitoringLogService;

/**
 * Implementation of SessionRegistry that performs audit logging of login and logout.
 */
public class LoggingSessionRegistryImpl extends SessionRegistryImpl {

    private MonitoringLogService monitoringService;

    public LoggingSessionRegistryImpl(MonitoringLogService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        if (principal instanceof IntygsadminUser user) {
            monitoringService.logUserLogin(user.getEmployeeHsaId(), user.getAuthenticationMethod());
        }
        super.registerNewSession(sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation != null) {
            Object principal = sessionInformation.getPrincipal();

            if (principal instanceof IntygsadminUser) {
                IntygsadminUser user = (IntygsadminUser) principal;
                if (sessionInformation.isExpired()) {
                    monitoringService.logUserSessionExpired(user.getEmployeeHsaId(), user.getAuthenticationMethod());
                } else {
                    monitoringService.logUserLogout(user.getEmployeeHsaId(), user.getAuthenticationMethod());
                }
            }
        }
        super.removeSessionInformation(sessionId);
    }
}
