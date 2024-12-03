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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import se.inera.intyg.intygsadmin.web.controller.dto.IdToken;

public class CustomAuthorizationResolver implements OAuth2AuthorizationRequestResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OAuth2AuthorizationRequestResolver defaultResolver;
    private final Map<String, IdToken> idToken = Map.of("id_token", new IdToken());

    public CustomAuthorizationResolver(ClientRegistrationRepository repo) {
        defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        final var authRequest = defaultResolver.resolve(request);

        if (authRequest == null) {
            return null;
        }

        return customizeAuthorizationRequest(authRequest);
    }


    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        return defaultResolver.resolve(request, clientRegistrationId);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authRequest) {
        try {
            final var extraParams = new HashMap<String, Object>();
            extraParams.put("claims", objectMapper.writeValueAsString(idToken));
            return OAuth2AuthorizationRequest.from(authRequest)
                .additionalParameters(extraParams)
                .scope("openid").build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
