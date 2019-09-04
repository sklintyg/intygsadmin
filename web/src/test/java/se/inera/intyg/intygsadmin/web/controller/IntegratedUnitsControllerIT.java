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

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;

public class IntegratedUnitsControllerIT extends BaseRestIntegrationTest {

    private static final String INTEGRATED_UNITS_API_ENDPOINT = "/api/integratedUnits";

    @Test
    public void testGetIntegratedUnitOk() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        final String existingUnitId = "SE4815162344-1A01";

        given().expect().statusCode(OK)
            .when()
            .get(INTEGRATED_UNITS_API_ENDPOINT + "/" + existingUnitId)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-integrated-unit-response-schema.json"));
    }

    @Test
    public void testGetIntegratedUnitNotFound() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        final String nonExistingUnitId = "SE4815162344-XXXX";

        given().expect().statusCode(NOT_FOUND)
            .when()
            .get(INTEGRATED_UNITS_API_ENDPOINT + "/" + nonExistingUnitId);
    }

    @Test
    public void testGetIntegratedUnitsFile() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
            .when()
            .get(INTEGRATED_UNITS_API_ENDPOINT + "/file")
            .then()
            .contentType(is(MediaType.APPLICATION_OCTET_STREAM.toString()));
    }

}
