/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IneraOidcFilter extends AbstractAuthenticationProcessingFilter {

    private String clientId;
    private IDTokenValidator idTokenValidator;

    private OAuth2RestTemplate restTemplate;

    private UserPersistenceService userPersistenceService;

    public IneraOidcFilter(String defaultFilterProcessesUrl, OIDCProviderMetadata oidcProviderMetadata,
            String clientId, OAuth2RestTemplate restTemplate, UserPersistenceService userPersistenceService) {
        super(defaultFilterProcessesUrl);
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.userPersistenceService = userPersistenceService;
        setAuthenticationManager(new NoopAuthenticationManager()); // TODO Check!
        try {
            idTokenValidator = new IDTokenValidator(oidcProviderMetadata.getIssuer(), new ClientID(clientId), JWSAlgorithm.RS256,
                    oidcProviderMetadata.getJWKSetURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (final OAuth2Exception e) {
            throw new BadCredentialsException("Could not obtain access token", e);
        }
        try {
            final String idTokenString = accessToken.getAdditionalInformation().get("id_token").toString();
            JWT jwtIdToken = JWTParser.parse(idTokenString);
            IDTokenClaimsSet idTokenClaimsSet = idTokenValidator.validate(jwtIdToken, null);

            final String employeeHsaId = idTokenClaimsSet.getStringClaim("employeeHsaId");
            UserEntity userEntity = userPersistenceService
                    .findByEmployeeHsaId(employeeHsaId);

            // UserEntity userEntity = authorityOptional.orElseThrow(
            // () -> new BadCredentialsException("Failed authentication. No authority for employeeHsaId " + employeeHsaId));

            final IneraOidcUserDetails user = new IneraOidcUserDetails(userEntity, accessToken);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } catch (final Exception e) {
            throw new BadCredentialsException("Could not obtain user details from token", e);
        }

    }

    private static class NoopAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }

    }
}
