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
package se.inera.intyg.intygsadmin.web.auth;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;

@Getter
public class IntygsadminUser extends DefaultOidcUser implements Serializable {

    private final UUID id;
    private final String employeeHsaId;
    private final String name;
    private final OidcIdToken token;
    private final IntygsadminRole intygsadminRole;
    private final AuthenticationMethod authenticationMethod;

    public IntygsadminUser(UserEntity userEntity, AuthenticationMethod authenticationMethod, OidcIdToken token,
        Set<GrantedAuthority> authorities, String nameAttributeKey) {
        super(authorities, token, nameAttributeKey);
        this.id = userEntity.getId();
        this.employeeHsaId = userEntity.getEmployeeHsaId();
        this.intygsadminRole = userEntity.getIntygsadminRole();
        this.authenticationMethod = authenticationMethod;
        this.token = token;
        this.name = userEntity.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        IntygsadminUser that = (IntygsadminUser) o;
        return Objects.equals(id, that.id) && Objects.equals(employeeHsaId, that.employeeHsaId) && Objects.equals(
            name, that.name) && Objects.equals(token, that.token) && intygsadminRole == that.intygsadminRole
            && authenticationMethod == that.authenticationMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, employeeHsaId, name, token, intygsadminRole, authenticationMethod);
    }
}
