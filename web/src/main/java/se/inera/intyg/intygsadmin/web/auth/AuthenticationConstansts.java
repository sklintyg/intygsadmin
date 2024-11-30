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

public final class AuthenticationConstansts {

    public static final String LOGIN_URL = "/oauth2/authorization/siths";
    public static final String LOGIN_REDIRECT_URL = "/login/inera";
    public static final String LOGOUT_URL = "/logout";
    public static final String FAKE_PROFILE = "fake";
    public static final String FAKE_LOGIN_ENDPOINT = "/fake";
    public static final String FAKE_LOGIN_URL = "/welcome.html";
    public static final String SUCCESSFUL_LOGOUT_REDIRECT_URL = "/#/loggedout/m";
    public static final String TIMEOUT_LOGOUT_REDIRECT_URL = "/#/loggedout/LOGIN_FEL003";

    // Prevent instantiation
    private AuthenticationConstansts() {
    }
}
