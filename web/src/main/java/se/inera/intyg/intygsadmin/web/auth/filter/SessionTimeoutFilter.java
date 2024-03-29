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

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionTimeoutFilter extends OncePerRequestFilter {

    public static final String SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY = SessionTimeoutFilter.class.getName() + ".secondsToLive";

    private static final Logger LOG = LoggerFactory.getLogger(SessionTimeoutFilter.class);

    static final String LAST_ACCESS_TIME_ATTRIBUTE_NAME = SessionTimeoutFilter.class.getName() + ".SessionLastAccessTime";

    private static final long MILLISECONDS_PER_SECONDS = 1000;

    private String getSessionStatusUri;

    private SessionRegistry sessionRegistry;

    public SessionTimeoutFilter(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        checkSessionValidity(request);

        filterChain.doFilter(request, response);
    }

    private void checkSessionValidity(HttpServletRequest request) {
        // Get existing session - if any
        HttpSession session = request.getSession(false);

        // Is it a request that should'nt prolong the expiration?
        boolean isSessionStatusRequest = request.getRequestURI().contains(getSessionStatusUri);
        if (session != null) {
            Long lastAccess = (Long) session.getAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME);

            // Set an request attribute that other parties further down the request chaing can use.
            Long msUntilExpire = updateTimeLeft(request, session);

            if (msUntilExpire <= 0) {
                LOG.info("Session expired " + msUntilExpire + " ms ago. Invalidating it now!");
                SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
                if (sessionInformation != null) {
                    sessionInformation.expireNow();
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
        Long lastAccess = (Long) session.getAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME);
        long inactiveTime = (lastAccess == null) ? 0 : (System.currentTimeMillis() - lastAccess);
        long maxInactiveTime = session.getMaxInactiveInterval() * MILLISECONDS_PER_SECONDS;

        long msUntilExpire = maxInactiveTime - inactiveTime;
        request.setAttribute(SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY, msUntilExpire / MILLISECONDS_PER_SECONDS);
        return msUntilExpire;
    }

    public String getGetSessionStatusUri() {
        return getSessionStatusUri;
    }

    public void setGetSessionStatusUri(String getSessionStatusUri) {
        this.getSessionStatusUri = getSessionStatusUri;
    }

}
