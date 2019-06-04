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

package se.inera.intyg.intygsadmin.web.auth.fake;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_ENDPOINT;

public class FakeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FakeAuthenticationFilter.class);

    public FakeAuthenticationFilter() {
        super(FAKE_LOGIN_ENDPOINT);
        LOG.error("FakeAuthentication enabled. DO NOT USE IN PRODUCTION");
        setAuthenticationManager(new NoopAuthenticationManager());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        String parameter = request.getParameter("userJsonDisplay");
        // we manually encode the json parameter
        String json = URLDecoder.decode(parameter, "UTF-8");

        return performFakeAuthentication(json);
    }

    private Authentication performFakeAuthentication(String json) {

        try {
            FakeUser fakeUser = new ObjectMapper().readValue(json, FakeUser.class);
            LOG.info("Detected fake user {}", fakeUser);

            UserEntity userEntity = new UserEntity();
            userEntity.setEmployeeHsaId(fakeUser.getEmployeeHsaId());
            userEntity.setIntygsadminRole(IntygsadminRole.valueOf(fakeUser.getIntygsadminRole()));

            final IntygsadminUser user = new IntygsadminUser(userEntity, AuthenticationMethod.FAKE, null, fakeUser.getName());

            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        } catch (IOException e) {
            String message = "Failed to parse JSON for fake user: " + json;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    private static class NoopAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }

    }

}
