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

import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_URL;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.SUCCESSFUL_LOGOUT_REDIRECT_URL;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CustomLogoutSuccessHandler extends OidcClientInitiatedLogoutSuccessHandler {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    public CustomLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        super(clientRegistrationRepository);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        boolean isRedirected = false;
        if (authentication != null) {
            final Object principal = authentication.getPrincipal();
            if (principal instanceof IntygsadminUser intygsadminUser) {

                if (AuthenticationMethod.OIDC.equals(intygsadminUser.getAuthenticationMethod())) {
                    final var tokens = intygsadminUser.getToken();
                    if (tokens != null) {
                        final String idToken = intygsadminUser.getToken().getTokenValue();

                        if (StringUtils.hasText(idToken)) {
                            /*
                             * If we have an ID_TOKEN we use this to end the sso session at the IdP/OP.
                             * This will work in the most cases and the user will end up back in the application,
                             * but in rare cases, when the ID_TOKEN is no longer present in the IdP/OP, then the
                             * end user will instead get a "logged out" page from the IdP.
                             */

                            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                                .fromUriString(clientRegistrationRepository.findByRegistrationId("siths").getProviderDetails()
                                    .getConfigurationMetadata().get("end_session_endpoint").toString())
                                //.fromUri(ineraOIDCProviderMetadata.getEndSessionEndpointURI())
                                .queryParam("id_token_hint", idToken)
                                .queryParam("post_logout_redirect_uri", "https://ia.localtest.me/#/loggedout/m");
                                //.queryParam("post_logout_redirect_uri", idpProperties.getLogoutRedirectUri().toString());

                            isRedirected = true;
                            getRedirectStrategy().sendRedirect(request, response, uriBuilder.toUriString());

                        }
                    }
                } else {
                    // FAKE login and the user will end up back at the welcome.html page

                    isRedirected = true;
                    getRedirectStrategy().sendRedirect(request, response, FAKE_LOGIN_URL);
                }
            }
        }
        if (!isRedirected) {
            // Just send the user to the loggedOut url
            getRedirectStrategy().sendRedirect(request, response, SUCCESSFUL_LOGOUT_REDIRECT_URL);
        }
    }
}
