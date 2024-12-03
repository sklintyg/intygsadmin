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

package se.inera.intyg.intygsadmin.web.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IdpProperties;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.auth.fake.FakeUser;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {IdpProperties.class})
public class FakeLoginService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserPersistenceService userPersistenceService;
    private final IdpProperties idpProperties;

    private static final String FAKE_OIDC_ID_TOKEN = "fakeOidcIdToken";

    public void login(FakeUser fakeUser, HttpServletRequest request) {
        final var oldSession = request.getSession(false);
        Optional.ofNullable(oldSession).ifPresent(HttpSession::invalidate);

        final var now = Instant.now();
        final var userEntity = getUserEntity(fakeUser);
        final var claims = Map.<String, Object>of(idpProperties.getUserNameAttributeName(), fakeUser.getEmployeeHsaId());
        final var oidcToken = new OidcIdToken(FAKE_OIDC_ID_TOKEN, now, now.plus(1, ChronoUnit.HOURS), claims);
        final var grantedAuthority = new SimpleGrantedAuthority("ROLE_" + userEntity.getIntygsadminRole().name());
        final var user = new IntygsadminUser(userEntity, AuthenticationMethod.FAKE, oidcToken, Set.of(grantedAuthority),
            idpProperties.getUserNameAttributeName());

        final var context = SecurityContextHolder.createEmptyContext();
        final var authentication = new UsernamePasswordAuthenticationToken(user, oidcToken, Collections.emptySet());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        final var newSession = request.getSession(true);
        newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        applicationEventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authentication, this.getClass()));
    }

    public void logout(HttpSession session) {
        if (session == null) {
            return;
        }
        session.invalidate();

        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        applicationEventPublisher.publishEvent(new LogoutSuccessEvent(authentication));
    }

    private UserEntity getUserEntity(FakeUser fakeUser) {
        return userPersistenceService.findByEmployeeHsaId(fakeUser.getEmployeeHsaId())
            .orElseThrow(() -> new BadCredentialsException("Authentication failed. No IntygsadminUser found for employeeHsaId "
                + fakeUser.getEmployeeHsaId()));
    }
}
