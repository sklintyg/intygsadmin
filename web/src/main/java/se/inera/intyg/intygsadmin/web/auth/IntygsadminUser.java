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

package se.inera.intyg.intygsadmin.web.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class IntygsadminUser implements UserDetails {

    private String userId;
    private String name;
    private OAuth2AccessToken token;
    private IntygsadminRole intygsadminRole;
    private AuthenticationMethod authenticationMethod;

    public IntygsadminUser(UserEntity userEntity, AuthenticationMethod authenticationMethod, OAuth2AccessToken token, String name) {
        this.userId = userEntity.getEmployeeHsaId();
        this.intygsadminRole = userEntity.getIntygsadminRole();
        this.authenticationMethod = authenticationMethod;
        this.token = token;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(intygsadminRole.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
