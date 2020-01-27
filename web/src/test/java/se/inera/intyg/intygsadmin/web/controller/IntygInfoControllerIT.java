/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;

public class IntygInfoControllerIT extends BaseRestIntegrationTest {

    private static final String INTYG_INFO_API_ENDPOINT = "/api/intygInfo";

    @Test
    public void testGetIntygInfoOk() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        final String existingIntygId = "f63c813d-a13a-4b4b-965f-419dfe98fffe";

        given().expect().statusCode(OK)
            .when()
            .get(INTYG_INFO_API_ENDPOINT + "/" + existingIntygId)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-intyg-info-response-schema.json"));
    }

    @Test
    public void testGetIntygInfotNotFound() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        final String nonExistingIntygId = "9ae0c3b4-3d80-46f3-acde-b332970ba0ea";

        given().expect().statusCode(NOT_FOUND)
            .when()
            .get(INTYG_INFO_API_ENDPOINT + "/" + nonExistingIntygId);
    }

    @Test
    public void testGetList() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
            .when()
            .get(INTYG_INFO_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-intyg-info-list-response-schema.json"));
    }

}
