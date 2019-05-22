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

import org.junit.jupiter.api.Test;

import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PublicApiControllerIT extends BaseRestIntegrationTest {

    private static final String PUBLIC_API_ENDPOINT = "/public-api";

    @Test
    public void testGetAppConfig() {
        given().expect().statusCode(OK)
                .when()
                .get(PUBLIC_API_ENDPOINT + "/appconfig")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-appconfig-response-schema.json"));
    }

    @Test
    public void testGetVersionInfo() {
        given().expect().statusCode(OK)
                .when()
                .get(PUBLIC_API_ENDPOINT + "/version")
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-version-response-schema.json"));
    }

}