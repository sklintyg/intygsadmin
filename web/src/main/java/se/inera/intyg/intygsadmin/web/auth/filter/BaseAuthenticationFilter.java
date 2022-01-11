/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.web.auth.filter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;

public abstract class BaseAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected UserPersistenceService userPersistenceService;

    public BaseAuthenticationFilter(String defaultFilterProcessesUrl, UserPersistenceService userPersistenceService) {
        super(defaultFilterProcessesUrl);
        this.userPersistenceService = userPersistenceService;

        setAuthenticationManager(new NoopAuthenticationManager());
    }

    protected UsernamePasswordAuthenticationToken createAuthentication(String hsaId, AuthenticationMethod authenticationMethod,
        OAuth2AccessToken accessToken) {
        // CHECKSTYLE:OFF Indentation
        UserEntity userEntity = userPersistenceService
            .findByEmployeeHsaId(hsaId).orElseThrow(
                () -> new BadCredentialsException(
                    "Failed authentication. No IntygsadminUser for employeeHsaId " + hsaId));
        // CHECKSTYLE:ON Indentation

        final IntygsadminUser user = new IntygsadminUser(userEntity, authenticationMethod, accessToken);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getIntygsadminRole().name()));

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    private static class NoopAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }

    }
}
