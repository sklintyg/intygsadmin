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
package se.inera.intyg.intygsadmin.web.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_URL;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_PROFILE;
import static se.inera.intyg.intygsadmin.web.auth.fake.FakeApiController.FAKE_API_REQUEST_MAPPING;
import static se.inera.intyg.intygsadmin.web.controller.PublicApiController.PUBLIC_API_REQUEST_MAPPING;
import static se.inera.intyg.intygsadmin.web.controller.PublicApiController.SESSION_STAT_REQUEST_MAPPING;
import static se.inera.intyg.intygsadmin.web.controller.RequestErrorController.IA_SPRING_SEC_ERROR_CONTROLLER_PATH;
import static se.inera.intyg.intygsadmin.web.controller.UserController.API_ANVANDARE;

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts;
import se.inera.intyg.intygsadmin.web.auth.IdpProperties;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminLogoutSuccessHandler;
import se.inera.intyg.intygsadmin.web.auth.LoggingForwardAuthenticationFailureHandler;
import se.inera.intyg.intygsadmin.web.auth.LoggingSessionRegistryImpl;
import se.inera.intyg.intygsadmin.web.auth.fake.FakeAuthenticationFilter;
import se.inera.intyg.intygsadmin.web.auth.filter.IndividualClaimsOuth2ContextFilter;
import se.inera.intyg.intygsadmin.web.auth.filter.IneraOidcFilter;
import se.inera.intyg.intygsadmin.web.auth.filter.SessionTimeoutFilter;
import se.inera.intyg.intygsadmin.web.service.monitoring.MonitoringLogServiceImpl;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(value = {IdpProperties.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final OIDCProviderMetadata ineraOIDCProviderMetadata;
    private final OAuth2RestTemplate restTemplate;
    private final UserPersistenceService userPersistenceService;
    private final IdpProperties idpProperties;

    private final List<String> profiles;

    public SecurityConfig(OIDCProviderMetadata ineraOIDCProviderMetadata, OAuth2RestTemplate restTemplate,
        UserPersistenceService userPersistenceService, IdpProperties idpProperties, Environment environment) {
        this.ineraOIDCProviderMetadata = ineraOIDCProviderMetadata;
        this.restTemplate = restTemplate;
        this.userPersistenceService = userPersistenceService;
        this.idpProperties = idpProperties;
        this.profiles = Arrays.asList(environment.getActiveProfiles());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry loggingSessionRegistry() {
        return new LoggingSessionRegistryImpl(new MonitoringLogServiceImpl());
    }

    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(loggingSessionRegistry());
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successRedirectHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/");
        handler.setAlwaysUseDefaultTargetUrl(true);
        return handler;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new LoggingForwardAuthenticationFailureHandler(IA_SPRING_SEC_ERROR_CONTROLLER_PATH);
    }

    @Bean
    public IndividualClaimsOuth2ContextFilter outh2ContextFilter() {
        return new IndividualClaimsOuth2ContextFilter(idpProperties.getRequestedClaims());
    }

    @Bean
    public IneraOidcFilter ineraOidcFilter() {
        IneraOidcFilter ineraOidcFilter = new IneraOidcFilter(idpProperties.getRedirectUri().getPath(), ineraOIDCProviderMetadata,
            idpProperties.getClientId(),
            restTemplate, userPersistenceService);
        ineraOidcFilter.setAuthenticationSuccessHandler(successRedirectHandler());
        ineraOidcFilter.setAuthenticationFailureHandler(failureHandler());
        ineraOidcFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        return ineraOidcFilter;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new IntygsadminLogoutSuccessHandler(ineraOIDCProviderMetadata, idpProperties);
    }

    @Bean
    @Profile({FAKE_PROFILE})
    public FakeAuthenticationFilter fakeAuthenticationFilter() {
        FakeAuthenticationFilter fakeAuthenticationFilter = new FakeAuthenticationFilter(userPersistenceService);
        fakeAuthenticationFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        fakeAuthenticationFilter.setAuthenticationSuccessHandler(successRedirectHandler());
        fakeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler());
        return fakeAuthenticationFilter;
    }

    @Bean
    public FilterRegistrationBean<SessionTimeoutFilter> sessionTimeoutFilter() {
        final SessionTimeoutFilter sessionTimeoutFilter = new SessionTimeoutFilter(loggingSessionRegistry());
        sessionTimeoutFilter.setGetSessionStatusUri(SESSION_STAT_REQUEST_MAPPING);

        FilterRegistrationBean<SessionTimeoutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(sessionTimeoutFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // All static client resources could be completely ignored by Spring Security.
        // This is also needed for a IE11 font loading bug where Springs Security default no-cache headers
        // will stop IE from loading fonts properly.
        return (web) -> web.ignoring().requestMatchers(antMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(antMatcher("/")).permitAll()
                .requestMatchers(antMatcher("/version.html")).permitAll()
                .requestMatchers(antMatcher("/public-api/version")).permitAll()
                .requestMatchers(antMatcher("/version-assets/**")).permitAll()
                .requestMatchers(antMatcher("/favicon*")).permitAll()
                .requestMatchers(antMatcher("/index.html")).permitAll()
                .requestMatchers(antMatcher("/images/**")).permitAll()
                .requestMatchers(antMatcher("/app/**")).permitAll()
                .requestMatchers(antMatcher("/assets/**")).permitAll()
                .requestMatchers(antMatcher("/components/**")).permitAll()
                .requestMatchers(antMatcher("/actuator/**")).permitAll()
                .requestMatchers(antMatcher(API_ANVANDARE)).permitAll()
                .requestMatchers(antMatcher(PUBLIC_API_REQUEST_MAPPING + "/**")).permitAll()
                .anyRequest().fullyAuthenticated()
            )
            .requestCache(cacheConfigurer -> cacheConfigurer
                .requestCache(new HttpSessionRequestCache()
                )
            )
            .csrf(AbstractHttpConfigurer::disable);




        if (profiles.contains(FAKE_PROFILE)) {
            addFakeLogin(http);
        } else {
            denyFakeLogin(http);
        }

        configureOpenApi(http);
        configureOidc(http);
        http.csrf().disable();

        return http.build();
    }

    private void configureOpenApi(HttpSecurity http) throws Exception {
        if (profiles.contains("dev")) {
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(antMatcher("/swagger-ui.html")).permitAll()
                    .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                    .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                );
        }
    }

    private void configureOidc(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(antMatcher("/**")).fullyAuthenticated()
            )
            .exceptionHandling(exceptionHandler -> exceptionHandler
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint(idpProperties.getRedirectUri().getPath()),
                    new AntPathRequestMatcher(AuthenticationConstansts.LOGIN_URL)
                )
                .defaultAuthenticationEntryPointFor(
                    new Http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE
                )

            )
            .sessionManagement(sessionConfigurer -> sessionConfigurer
                .sessionAuthenticationStrategy(registerSessionAuthenticationStrategy())
            )
            .logout(logoutSuccessHandlerConfigurer -> logoutSuccessHandlerConfigurer
                .logoutSuccessHandler(logoutSuccessHandler())
            )

            .addFilterAfter(outh2ContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(ineraOidcFilter(), IndividualClaimsOuth2ContextFilter.class)
            .logout()
            .invalidateHttpSession(true)
            .logoutUrl(AuthenticationConstansts.LOGOUT_URL)
            .logoutSuccessHandler(logoutSuccessHandler())
            .clearAuthentication(true)

        // @formatter:on

    }

    private void denyFakeLogin(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(antMatcher(FAKE_LOGIN_URL)).denyAll()
                .requestMatchers(antMatcher("/welcome-assets/**")).denyAll()
                .requestMatchers(antMatcher("/h2-console/**")).denyAll()
                .requestMatchers(antMatcher(FAKE_API_REQUEST_MAPPING + "/**")).denyAll()
            );
    }

    private void addFakeLogin(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(antMatcher(FAKE_LOGIN_URL)).permitAll()
                .requestMatchers(antMatcher("/welcome-assets/**")).permitAll()
                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(antMatcher(FAKE_API_REQUEST_MAPPING + "/**")).permitAll()
            )
            .headers(headersConfigurer -> headersConfigurer
                    .frameOptions(FrameOptionsConfig::sameOrigin))
            .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
    }
}
