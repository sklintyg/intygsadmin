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
package se.inera.intyg.intygsadmin.web.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static se.inera.intyg.intygsadmin.web.controller.PublicApiController.PUBLIC_API_REQUEST_MAPPING;
import static se.inera.intyg.intygsadmin.web.controller.PublicApiController.SESSION_STAT_REQUEST_MAPPING;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;

public class PublicApiControllerIT extends BaseRestIntegrationTest {

    @Test
    public void testGetAppConfig() {
        given().expect().statusCode(OK)
            .when()
            .get(PUBLIC_API_REQUEST_MAPPING + "/appconfig")
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-appconfig-response-schema.json"));
    }

    @Test
    public void testGetVersionInfo() {
        given().expect().statusCode(OK)
            .when()
            .get(PUBLIC_API_REQUEST_MAPPING + "/version")
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-version-response-schema.json"));
    }

    @Test
    public void testGetSessionStatus() {

        given().expect().statusCode(OK)
            .when()
            .get(SESSION_STAT_REQUEST_MAPPING)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-sessionstatus-response-schema.json"))
            .body("sessionState.hasSession", is(false))
            .body("sessionState.authenticated", is(false))
            .body("sessionState.secondsUntilExpire", is(0));

        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
            .when()
            .get(SESSION_STAT_REQUEST_MAPPING)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-sessionstatus-response-schema.json"))
            .body("sessionState.hasSession", is(true))
            .body("sessionState.authenticated", is(true))
            .body("sessionState.secondsUntilExpire", greaterThan(0));
    }

}
