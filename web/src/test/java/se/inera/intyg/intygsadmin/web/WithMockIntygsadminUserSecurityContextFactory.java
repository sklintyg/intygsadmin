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

package se.inera.intyg.intygsadmin.web;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;

import java.util.ArrayList;

public class WithMockIntygsadminUserSecurityContextFactory implements WithSecurityContextFactory<WithMockIntygsadminUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockIntygsadminUser mockIntygsadminUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmployeeHsaId(mockIntygsadminUser.employeeHsaId());
        userEntity.setIntygsadminRole(IntygsadminRole.valueOf(mockIntygsadminUser.intygsadminRole()));

        final IntygsadminUser user = new IntygsadminUser(userEntity, AuthenticationMethod.FAKE, null, mockIntygsadminUser.name());
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));

        return context;
    }
}
