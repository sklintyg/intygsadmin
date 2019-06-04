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

package se.inera.intyg.intygsadmin.web.controller.endpoint;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsadmin.web.controller.dto.UserEntityDTO;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class UserEndpointIT extends BaseRestIntegrationTest {

    private static final String USER_ACTUATOR_ENDPOINT = "/actuator/user";
    private final UserEntityDTO user = new UserEntityDTO(null, "TEST-HSA-1", "ADMIN");

    public UserEndpointIT() {
        baseUrl = System.getProperty("integration.tests.actuatorUrl", "http://localhost:8681");
    }

    @Test
    public void testGetUsers() {
        addUser();

        given().expect().statusCode(OK)
                .when()
                .get(USER_ACTUATOR_ENDPOINT)
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-users-actuator-response-schema.json"));
    }

    @Test
    public void testGetUser() {
        addUser();

        given().expect().statusCode(OK)
                .when()
                .get(USER_ACTUATOR_ENDPOINT + "/" + user.getEmployeeHsaId())
                .then()
                .body("employeeHsaId", equalTo(user.getEmployeeHsaId()));
    }

    @Test
    public void testDeleteUser() {
        addUser();

        given().expect().statusCode(OK)
                .when()
                .get(USER_ACTUATOR_ENDPOINT + "/" + user.getEmployeeHsaId())
                .then()
                .body("employeeHsaId", equalTo(user.getEmployeeHsaId()));

        given().expect().statusCode(OK)
                .when()
                .delete(USER_ACTUATOR_ENDPOINT + "/" + user.getEmployeeHsaId());

        given().expect().statusCode(OK)
                .when()
                .get(USER_ACTUATOR_ENDPOINT + "/" + user.getEmployeeHsaId())
                .then()
                .body("employeeHsaId", equalTo(null));
    }

    @Test
    public void testUpsertUser() {
        addUser();

        given().expect().statusCode(OK)
                .when()
                .get(USER_ACTUATOR_ENDPOINT + "/" + user.getEmployeeHsaId())
                .then()
                .body("intygsadminRole", equalTo(user.getIntygsadminRole()));

        user.setIntygsadminRole("BASIC");
        with().body(user).contentType(ContentType.JSON).post(USER_ACTUATOR_ENDPOINT);

        given().expect().statusCode(OK)
                .when()
                .get(USER_ACTUATOR_ENDPOINT + "/" + user.getEmployeeHsaId())
                .then()
                .body("intygsadminRole", equalTo(user.getIntygsadminRole()));
    }

    private void addUser() {
        with().body(user).contentType(ContentType.JSON).post(USER_ACTUATOR_ENDPOINT);
    }
}
