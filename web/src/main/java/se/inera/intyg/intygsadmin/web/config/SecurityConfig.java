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

import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_URL;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_PROFILE;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.LOGIN_REDIRECT_URL;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.LOGOUT_URL;
import static se.inera.intyg.intygsadmin.web.auth.fake.FakeApiController.FAKE_API_REQUEST_MAPPING;
import static se.inera.intyg.intygsadmin.web.controller.PublicApiController.PUBLIC_API_REQUEST_MAPPING;
import static se.inera.intyg.intygsadmin.web.controller.RequestErrorController.IA_SPRING_SEC_ERROR_CONTROLLER_PATH;
import static se.inera.intyg.intygsadmin.web.controller.UserController.API_ANVANDARE;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.CustomAuthorizationResolver;
import se.inera.intyg.intygsadmin.web.auth.CustomLogoutSuccessHandler;
import se.inera.intyg.intygsadmin.web.auth.IdpProperties;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.auth.filter.SessionTimeoutFilter;
import se.inera.intyg.intygsadmin.web.service.monitoring.MonitoringLogService;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(value = {IdpProperties.class})
public class SecurityConfig {

    private final UserPersistenceService userPersistenceService;
    private final IdpProperties idpProperties;
    private final List<String> profiles;

    public SecurityConfig(UserPersistenceService userPersistenceService, IdpProperties idpProperties, Environment environment) {
        this.userPersistenceService = userPersistenceService;
        this.idpProperties = idpProperties;
        this.profiles = Arrays.asList(environment.getActiveProfiles());
    }

    @Bean
    public FilterRegistrationBean<SessionTimeoutFilter> sessionTimeoutFilter(MonitoringLogService monitoringLogService) {
        final SessionTimeoutFilter sessionTimeoutFilter = new SessionTimeoutFilter(monitoringLogService);

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
        return web -> web.ignoring().requestMatchers("/static/**");
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        final var registration = ClientRegistrations.fromOidcIssuerLocation(idpProperties.getIssuerUri())
            .registrationId(idpProperties.getClientRegistrationId())
            .clientId(idpProperties.getClientId())
            .clientSecret(idpProperties.getClientSecret())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(idpProperties.getRedirectUri().toString())
            .userNameAttributeName(idpProperties.getUserNameAttributeName())
            .scope(idpProperties.getScope())
            .build();
        return new InMemoryClientRegistrationRepository(registration);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        CustomLogoutSuccessHandler customLogoutSuccessHandler) throws Exception {

        configureFakeLogin(http, profiles);
        configureOpenApi(http);

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/version.html").permitAll()
                .requestMatchers("/public-api/version").permitAll()
                .requestMatchers("/version-assets/**").permitAll()
                .requestMatchers("/favicon*").permitAll()
                .requestMatchers("/index.html").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/app/**").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/components/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(API_ANVANDARE).permitAll()
                .requestMatchers(IA_SPRING_SEC_ERROR_CONTROLLER_PATH).permitAll()
                .requestMatchers(PUBLIC_API_REQUEST_MAPPING + "/**").permitAll()
                .anyRequest().fullyAuthenticated()
            )
            .oauth2Client(httpSecurityOAuth2ClientConfigurer -> httpSecurityOAuth2ClientConfigurer
                .authorizationCodeGrant(authorizationCodeGrantConfigurer -> authorizationCodeGrantConfigurer
                    .authorizationRequestResolver(new CustomAuthorizationResolver(clientRegistrationRepository()))
                )
            )
            .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                .failureHandler(new ForwardAuthenticationFailureHandler(IA_SPRING_SEC_ERROR_CONTROLLER_PATH))
                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
                    .baseUri(LOGIN_REDIRECT_URL)
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(this.oidcUserService()))
            )
            .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            )
            .exceptionHandling(exceptionHandler -> exceptionHandler
                .defaultAuthenticationEntryPointFor(new Http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE)
            )
            .csrf(AbstractHttpConfigurer::disable)
            .requestCache(cacheConfigurer -> cacheConfigurer
                .requestCache(new HttpSessionRequestCache())
            );

        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            final var oidcIdToken = userRequest.getIdToken();
            final var claims = oidcIdToken.getClaims();
            final var userHsaId = String.valueOf(claims.get(idpProperties.getUserNameAttributeName()));
            final var userEntity = getUserEntity(userHsaId);
            final var grantedAuthority = new SimpleGrantedAuthority("ROLE_" + userEntity.getIntygsadminRole().name());
            final var providerDetails = userRequest.getClientRegistration().getProviderDetails();
            final var userNameAttributeName = providerDetails.getUserInfoEndpoint().getUserNameAttributeName();

            return new IntygsadminUser(userEntity, AuthenticationMethod.OIDC, oidcIdToken, Set.of(grantedAuthority),
                userNameAttributeName);
        };
    }

    private UserEntity getUserEntity(String userHsaId) {
        return userPersistenceService.findByEmployeeHsaId(userHsaId)
            .orElseThrow(() -> new BadCredentialsException("Authentication failed. No IntygsadminUser for employeeHsaId " + userHsaId));
    }

    private void configureFakeLogin(HttpSecurity http, List<String> profiles) throws Exception {
        if (profiles.contains(FAKE_PROFILE)) {
            allowFakeLogin(http);
        } else {
            denyFakeLogin(http);
        }
    }

    private void denyFakeLogin(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(FAKE_LOGIN_URL).denyAll()
                .requestMatchers("/welcome-assets/**").denyAll()
                .requestMatchers("/h2-console/**").denyAll()
                .requestMatchers(FAKE_API_REQUEST_MAPPING + "/**").denyAll()
            );
    }

    private void allowFakeLogin(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(FAKE_LOGIN_URL).permitAll()
                .requestMatchers("/welcome-assets/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(FAKE_API_REQUEST_MAPPING + "/**").permitAll()
            );
    }

    private void configureOpenApi(HttpSecurity http) throws Exception {
        if (profiles.contains("dev")) {
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                );
        }
    }
}
