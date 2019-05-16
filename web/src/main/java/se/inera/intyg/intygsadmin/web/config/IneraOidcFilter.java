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

package se.inera.intyg.intygsadmin.web.config;

import com.auth0.jwk.Jwk;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.GeneralException;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TimeZone;

public class IneraOidcFilter extends AbstractAuthenticationProcessingFilter {

    private OIDCClientInformation oidcClientInformation;
    private IDTokenValidator idTokenValidator;

    public OAuth2RestOperations restTemplate;

    public IneraOidcFilter(String defaultFilterProcessesUrl, OIDCProviderMetadata oidcProviderMetadata,
            OIDCClientInformation oidcClientInformation) {
        super(defaultFilterProcessesUrl);
        this.oidcClientInformation = oidcClientInformation;
        setAuthenticationManager(new NoopAuthenticationManager());
        try {
            idTokenValidator = IDTokenValidator.create(oidcProviderMetadata, oidcClientInformation);
        } catch (GeneralException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (final OAuth2Exception e) {
            throw new BadCredentialsException("Could not obtain access token", e);
        }
        try {
            final String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
            String kid = JwtHelper.headers(idToken)
                    .get("kid");
            final Jwt tokenDecoded = JwtHelper.decodeAndVerify(idToken, verifier(kid));
            final Map<String, String> authInfo = new ObjectMapper().readValue(tokenDecoded.getClaims(), Map.class);
            verifyClaims(authInfo);
            final IneraOidcUserDetails user = new IneraOidcUserDetails(authInfo, accessToken);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } catch (final Exception e) {
            throw new BadCredentialsException("Could not obtain user details from token", e);
        }

    }

    public void verifyClaims(Map claims) {
        int exp = (int) claims.get("exp");
        LocalDateTime expireDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(exp),
                TimeZone.getDefault().toZoneId());
        if (expireDate.isBefore(LocalDateTime.now(TimeZone.getDefault().toZoneId())) || !claims.get("iss").equals(issuer)
                || !claims.get("aud").equals(clientId)) {
            throw new RuntimeException("Invalid claims");
        }
    }

    private URL buildUrl() {
        try {
            return new URL(jwkUri);
        } catch (MalformedURLException e) {
            throw new AuthenticationServiceException(
                    "Unable to construct URL for jwks from " + jwkUri + ". Message: " + e.getMessage());
        }
    }

    private RsaVerifier verifier(String kid) throws Exception {

        Jwk jwk = jwkProvider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    public void setRestTemplate(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    private static class NoopAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }

    }
}
