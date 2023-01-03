/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.web.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts;
import se.inera.intyg.intygsadmin.web.auth.filter.SessionTimeoutFilter;
import se.inera.intyg.intygsadmin.web.controller.dto.AppConfigDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.SessionState;
import se.inera.intyg.intygsadmin.web.controller.dto.SessionStateResponse;
import se.inera.intyg.intygsadmin.web.controller.dto.VersionInfoDTO;

@RestController
@RequestMapping(PublicApiController.PUBLIC_API_REQUEST_MAPPING)
public class PublicApiController {

    public static final String PUBLIC_API_REQUEST_MAPPING = "/public-api";
    public static final String SESSION_STATUS_PING = "/session-stat/ping";
    public static final String SESSION_STAT_REQUEST_MAPPING = PUBLIC_API_REQUEST_MAPPING + SESSION_STATUS_PING;

    private BuildProperties buildProperties;
    private Environment environment;

    @Autowired
    public PublicApiController(Optional<BuildProperties> buildProperties, Environment environment) {
        this.buildProperties = buildProperties.orElse(null);
        this.environment = environment;
    }

    @GetMapping(value = PublicApiController.SESSION_STATUS_PING)
    public SessionStateResponse getSessionStatus(HttpServletRequest request) {
        return createStatusResponse(request);
    }

    @GetMapping(path = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionInfoDTO> getVersion() {
        var versionInfo = getVersionDTO();

        return ResponseEntity.ok(versionInfo);
    }

    @GetMapping(path = "/appconfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppConfigDTO> getAppConfig() {
        var versionInfo = getVersionDTO();
        String loginUrl = AuthenticationConstansts.LOGIN_URL;

        var appConfig = new AppConfigDTO(loginUrl, versionInfo);

        return ResponseEntity.ok(appConfig);
    }

    private VersionInfoDTO getVersionDTO() {
        var applicationName = buildProperties != null ? buildProperties.getArtifact() : "N/A";
        var buildVersion = buildProperties != null ? buildProperties.getVersion() : "N/A";
        var buildTimestamp = buildProperties != null ? LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault())
            : LocalDateTime.now();
        var activeProfiles = StringUtils.join(environment.getActiveProfiles(), ", ");

        return new VersionInfoDTO(applicationName, buildVersion, buildTimestamp, activeProfiles);
    }

    private SessionStateResponse createStatusResponse(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // The sessionTimeoutFilter should have put a secondsLeft attribute in the request for us to use.
        Long secondsLeft = (Long) request.getAttribute(SessionTimeoutFilter.SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY);
        final boolean isAuthenticated = hasAuthenticatedPrincipalSession(session);
        return new SessionStateResponse(new SessionState(session != null, isAuthenticated,
            secondsLeft == null ? 0 : secondsLeft));
    }

    private boolean hasAuthenticatedPrincipalSession(HttpSession session) {
        if (session != null) {
            final Object context = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (context != null && context instanceof SecurityContext) {
                SecurityContext securityContext = (SecurityContext) context;
                return securityContext.getAuthentication() != null && securityContext.getAuthentication().getPrincipal() != null;
            }

        }
        return false;
    }

}
