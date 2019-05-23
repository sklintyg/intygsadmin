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

import com.google.common.collect.Lists;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.IdpProperties;
import se.inera.intyg.intygsadmin.web.auth.IndividualClaimsOuth2ContextFilter;
import se.inera.intyg.intygsadmin.web.auth.IneraOidcFilter;
import se.inera.intyg.intygsadmin.web.auth.IneraOidcLogoutSuccessHandler;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static se.inera.intyg.intygsadmin.web.controller.UserController.API_ANVANDARE;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(value = { IdpProperties.class })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private OIDCProviderMetadata ineraOIDCProviderMetadata;
    private OAuth2RestTemplate restTemplate;
    private UserPersistenceService userPersistenceService;
    private IdpProperties idpProperties;

    @Autowired
    public SecurityConfig(OIDCProviderMetadata ineraOIDCProviderMetadata, OAuth2RestTemplate restTemplate,
            UserPersistenceService userPersistenceService, IdpProperties idpProperties) {
        this.ineraOIDCProviderMetadata = ineraOIDCProviderMetadata;
        this.restTemplate = restTemplate;
        this.userPersistenceService = userPersistenceService;
        this.idpProperties = idpProperties;
    }

    @Bean
    public IndividualClaimsOuth2ContextFilter outh2ContextFilter() {
        return new IndividualClaimsOuth2ContextFilter(idpProperties.getRequestedClaims());
    }

    @Bean
    public IneraOidcFilter ineraOidcFilter() throws MalformedURLException {
        return new IneraOidcFilter(idpProperties.getRedirectUri().getPath(), ineraOIDCProviderMetadata, idpProperties.getClientId(),
                restTemplate, userPersistenceService);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new IneraOidcLogoutSuccessHandler(ineraOIDCProviderMetadata, idpProperties);
    }

    @Bean
    public static OrRequestMatcher loginRequestMatcher() {
        ArrayList<RequestMatcher> matchers = Lists.newArrayList(
                new AntPathRequestMatcher("/login*", null));
        return new OrRequestMatcher(matchers);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // All static client resources could be completely ignored by Spring Security.
        // This is also needed for a IE11 font loading bug where Springs Security default no-cache headers
        // will stop IE from loading fonts properly.
        web.ignoring().anyRequest();// antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // These should always be permitted
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/welcome-assets/**").permitAll()
                .antMatchers("/version.html").permitAll()
                .antMatchers("/public-api/version").permitAll()
                .antMatchers("/version-assets/**").permitAll()
                .antMatchers("/favicon*").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/components/**").permitAll()
                .antMatchers(API_ANVANDARE).permitAll();
        // .antMatchers(APPCONFIG_REQUEST_MAPPING).permitAll()
        // .antMatchers(SESSION_STAT_REQUEST_MAPPING + "/**").permitAll();

        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers("/**")
                    .fullyAuthenticated()
                .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint(idpProperties.getRedirectUri().getPath()), loginRequestMatcher())
                    .defaultAuthenticationEntryPointFor(new Http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE)
                .and()
                .addFilterAfter(outh2ContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(ineraOidcFilter(), IndividualClaimsOuth2ContextFilter.class)
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(logoutSuccessHandler());

        http.csrf().disable();
        // @formatter:on
    }

}
