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

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class OidcConfig {

    @Value("${inera.idp.client-id}")
    private String clientId;

    @Value("${inera.idp.client-secret}")
    private String clientSecret;

    @Value("${inera.idp.redirect-uri}")
    private String redirectUri;

    @Value("${inera.idp.issuer-uri}")
    private String issuerUri;

    @Bean
    public OAuth2ProtectedResourceDetails ineraOpenId(OIDCProviderMetadata ineraOIDCProviderMetadata) {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(ineraOIDCProviderMetadata.getTokenEndpointURI().toString());
        details.setUserAuthorizationUri(ineraOIDCProviderMetadata.getAuthorizationEndpointURI().toString());
        details.setScope(Arrays.asList("openid"));
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        details.setUseCurrentUri(false);
        details.setPreEstablishedRedirectUri(redirectUri);
        return details;
    }

    @Bean
    public OAuth2RestTemplate ineraOpenIdTemplate(OAuth2ClientContext clientContext, OIDCProviderMetadata ineraOIDCProviderMetadata) {
        return new OAuth2RestTemplate(ineraOpenId(ineraOIDCProviderMetadata), clientContext);
    }

    @Bean
    public OIDCProviderMetadata ineraOIDCProviderMetadata() {
        String openidConfiguration = getOpenidConfiguration(issuerUri);
        try {
            return OIDCProviderMetadata.parse(openidConfiguration);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getOpenidConfiguration(String issuer) {
        RestTemplate rest = new RestTemplate();
        try {
            return rest.getForObject(issuer + "/.well-known/openid-configuration", String.class);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Unable to resolve the OpenID Configuration with the provided Issuer of \"" + issuer + "\"",
                    e);
        }
    }
}