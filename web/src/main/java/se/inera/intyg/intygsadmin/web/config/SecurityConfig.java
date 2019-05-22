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

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.IndividualClaimsOuth2ContextFilter;
import se.inera.intyg.intygsadmin.web.auth.IneraOidcFilter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${inera.idp.client-id}")
    private String clientId;

    @Value("${inera.idp.redirect-uri}")
    private String redirectUri;

    @Value("${inera.idp.requested-claims}")
    private List<String> requestedClaims;

    @Autowired
    private OIDCProviderMetadata ineraOIDCProviderMetadata;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private UserPersistenceService userPersistenceService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/version-assets/**");
        web.ignoring().antMatchers("/welcome-assets/**");
        web.ignoring().antMatchers("/inera-login");
        web.ignoring().antMatchers("/index.html");
        web.ignoring().antMatchers("/version.html");
        web.ignoring().antMatchers("/welcome.html");
        web.ignoring().antMatchers("/error/**");
    }

    @Bean
    public IndividualClaimsOuth2ContextFilter outh2ContextFilter() {
        return new IndividualClaimsOuth2ContextFilter(requestedClaims);
    }

    @Bean
    public IneraOidcFilter ineraOidcFilter() throws MalformedURLException {
        return new IneraOidcFilter(new URL(redirectUri).getPath(), ineraOIDCProviderMetadata, clientId,
                restTemplate, userPersistenceService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(outh2ContextFilter(),
                        AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(ineraOidcFilter(), IndividualClaimsOuth2ContextFilter.class)
                .httpBasic().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(new URL(redirectUri).getPath()))
                .and()
                .authorizeRequests()
                // .antMatchers("/","/index*").permitAll()
                .anyRequest().authenticated();

        http.csrf().disable();

    }

}
