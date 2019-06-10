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

package se.inera.intyg.intygsadmin.web.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static se.inera.intyg.intygsadmin.web.controller.UserController.API_ANVANDARE;

class UserControllerIT extends BaseRestIntegrationTest {

    @Test
    void getUser() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
                .when()
                .get(API_ANVANDARE)
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-user-response-schema.json"))
                .body("logoutUrl", is(AuthenticationConstansts.LOGOUT_URL))
                .body("employeeHsaId", is(ADMIN_USER.getEmployeeHsaId()))
                .body("intygsadminRole", is(ADMIN_USER.getIntygsadminRole()))
                .body("name", is(ADMIN_USER.getName()));
    }
}