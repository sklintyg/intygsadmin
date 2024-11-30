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

import static se.inera.intyg.intygsadmin.web.controller.RequestErrorController.IA_SPRING_SEC_ERROR_CONTROLLER_PATH;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import se.inera.intyg.intygsadmin.web.service.monitoring.MonitoringLogService;

@Component
public class LoggingForwardAuthenticationFailureHandler extends ForwardAuthenticationFailureHandler {

    private final MonitoringLogService monitoringLogService;

    public LoggingForwardAuthenticationFailureHandler(MonitoringLogService monitoringLogService) {
        super(IA_SPRING_SEC_ERROR_CONTROLLER_PATH);
        this.monitoringLogService = monitoringLogService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        monitoringLogService.logFailedLogin(exception.getLocalizedMessage());
        super.onAuthenticationFailure(request, response, exception);
    }
}
