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
package se.inera.intyg.intygsadmin.web.auth.filter;

import static se.inera.intyg.infra.security.common.model.AuthConstants.SPRING_SECURITY_CONTEXT;
import static se.inera.intyg.intygsadmin.web.controller.PublicApiController.SESSION_STAT_REQUEST_MAPPING;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.filter.OncePerRequestFilter;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.service.monitoring.MonitoringLogService;

@Slf4j
@RequiredArgsConstructor
public class SessionTimeoutFilter extends OncePerRequestFilter {

    public static final String SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY = SessionTimeoutFilter.class.getName() + ".secondsToLive";
    private static final String LAST_ACCESS_TIME_ATTRIBUTE_NAME = SessionTimeoutFilter.class.getName() + ".SessionLastAccessTime";
    private static final long MILLISECONDS_PER_SECONDS = 1000;

    private final MonitoringLogService monitoringLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        checkSessionValidity(request);

        filterChain.doFilter(request, response);
    }

    private void checkSessionValidity(HttpServletRequest request) {
        // Get existing session - if any
        final var session = request.getSession(false);

        // Is it a request that should not prolong the expiration?
        boolean isSessionStatusRequest = request.getRequestURI().contains(SESSION_STAT_REQUEST_MAPPING);
        if (session != null) {
            final var  lastAccess = (Long) session.getAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME);

            // Set a request attribute that other parties further down the request chaing can use.
            final var  msUntilExpire = updateTimeLeft(request, session);
            final var context = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);

            if (msUntilExpire <= 0) {
                log.info("Session expired " + msUntilExpire + " ms ago. Invalidating it now!");

                if (context != null && context.getAuthentication() != null
                    && context.getAuthentication().getPrincipal() instanceof IntygsadminUser intygsadminUser) {
                    monitoringLogService.logUserSessionExpired(intygsadminUser.getEmployeeHsaId(),
                        intygsadminUser.getAuthenticationMethod());
                }

                session.invalidate();

            } else if (!isSessionStatusRequest || lastAccess == null) {
                // Update lastaccessed for ALL requests except status requests
                session.setAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME, System.currentTimeMillis());
                updateTimeLeft(request, session);
            }
        }
    }

    private Long updateTimeLeft(HttpServletRequest request, HttpSession session) {
        final var lastAccess = (Long) session.getAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME);
        final var inactiveTime = (lastAccess == null) ? 0 : (System.currentTimeMillis() - lastAccess);
        final var maxInactiveTime = session.getMaxInactiveInterval() * MILLISECONDS_PER_SECONDS;

        long msUntilExpire = maxInactiveTime - inactiveTime;
        request.setAttribute(SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY, msUntilExpire / MILLISECONDS_PER_SECONDS);
        return msUntilExpire;
    }

}
