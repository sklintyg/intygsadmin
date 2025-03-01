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

package se.inera.intyg.intygsadmin.web.auth;

import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_URL;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.SUCCESSFUL_LOGOUT_REDIRECT_URL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import se.inera.intyg.intygsadmin.web.service.FakeLoginService;

@Slf4j
@Component
public class CustomLogoutSuccessHandler extends OidcClientInitiatedLogoutSuccessHandler {

    private final IdpProperties idpProperties;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final FakeLoginService fakeLoginService;

    public CustomLogoutSuccessHandler(IdpProperties idpProperties, ClientRegistrationRepository clientRegistrationRepository,
        FakeLoginService fakeLoginService) {
        super(clientRegistrationRepository);
        this.idpProperties = idpProperties;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.fakeLoginService = fakeLoginService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException {

        if (authentication == null || !(authentication.getPrincipal() instanceof IntygsadminUser intygsadminUser)) {
            getRedirectStrategy().sendRedirect(request, response, SUCCESSFUL_LOGOUT_REDIRECT_URL);
            return;
        }

        if (!AuthenticationMethod.OIDC.equals(intygsadminUser.getAuthenticationMethod())) {
            fakeLoginService.logout(request.getSession(false));
            getRedirectStrategy().sendRedirect(request, response, FAKE_LOGIN_URL);
            return;
        }

        logoutSithsUser(request, response, intygsadminUser);
    }

    private void logoutSithsUser(HttpServletRequest request, HttpServletResponse response, IntygsadminUser user) throws IOException {
        final var oidcIdToken = user.getToken();

        if (oidcIdToken == null || !StringUtils.hasText(oidcIdToken.getTokenValue())) {
            getRedirectStrategy().sendRedirect(request, response, SUCCESSFUL_LOGOUT_REDIRECT_URL);
            log.warn("Failed to log out user since user token was null or empty");
            return;
        }

        final var idToken = user.getToken().getTokenValue();
        final var clientRegistration = clientRegistrationRepository.findByRegistrationId("siths");
        final var idpEndSessionEndpoint = clientRegistration.getProviderDetails().getConfigurationMetadata()
            .get("end_session_endpoint").toString();

        final var uriBuilder = UriComponentsBuilder.fromUriString(idpEndSessionEndpoint)
            .queryParam("id_token_hint", idToken)
            .queryParam("post_logout_redirect_uri", idpProperties.getLogoutRedirectUri());

        getRedirectStrategy().sendRedirect(request, response, uriBuilder.toUriString());
    }

}
