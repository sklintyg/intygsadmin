/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_ENDPOINT;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.filter.BaseAuthenticationFilter;
import se.inera.intyg.intygsadmin.web.exception.IaAuthenticationException;
import se.inera.intyg.intygsadmin.web.exception.IaErrorCode;

public class FakeAuthenticationFilter extends BaseAuthenticationFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FakeAuthenticationFilter.class);

    public FakeAuthenticationFilter(UserPersistenceService userPersistenceService) {
        super(FAKE_LOGIN_ENDPOINT, userPersistenceService);

        LOG.error("---- FakeAuthentication enabled. DO NOT USE IN PRODUCTION!!! ----");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        String parameter = request.getParameter("userJsonDisplay");
        // we manually encode the json parameter
        String json = URLDecoder.decode(parameter, StandardCharsets.UTF_8);

        return performFakeAuthentication(json);
    }

    private Authentication performFakeAuthentication(String json) {

        try {
            FakeUser fakeUser = new ObjectMapper().readValue(json, FakeUser.class);
            LOG.info("Detected fake user {}", fakeUser);

            if (StringUtils.isEmpty(fakeUser.getEmployeeHsaId())) {
                throw new BadCredentialsException("Failed authentication. No IntygsadminUser for employeeHsaId ");
            }

            return createAuthentication(fakeUser.getEmployeeHsaId(), AuthenticationMethod.FAKE, null);
        } catch (final BadCredentialsException exception) {
            LOG.warn(exception.getMessage());
            throw new IaAuthenticationException(IaErrorCode.LOGIN_FEL002, exception.getMessage(), UUID.randomUUID().toString());
        } catch (IOException e) {
            String message = "Failed to parse JSON for fake user: " + json;
            LOG.error(message, e);
            throw new IaAuthenticationException(IaErrorCode.LOGIN_FEL001, message, UUID.randomUUID().toString());
        }
    }
}
