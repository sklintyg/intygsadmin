/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;

public class WithMockIntygsadminUserSecurityContextFactory implements WithSecurityContextFactory<WithMockIntygsadminUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockIntygsadminUser mockIntygsadminUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName(mockIntygsadminUser.name());
        userEntity.setEmployeeHsaId(mockIntygsadminUser.employeeHsaId());
        userEntity.setIntygsadminRole(IntygsadminRole.valueOf(mockIntygsadminUser.intygsadminRole()));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getIntygsadminRole().name()));

        final var oidcIdToken = new OidcIdToken("tokenValue", null, null, Map.of("employeeHsaId", "hsaId"));
        final IntygsadminUser user = new IntygsadminUser(userEntity, AuthenticationMethod.FAKE, oidcIdToken,
            Collections.emptySet(), "employeeHsaId");
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, authorities));

        return context;
    }
}
