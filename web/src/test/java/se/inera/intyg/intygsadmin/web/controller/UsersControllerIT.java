/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsadmin.web.controller.dto.UserDTO;

public class UsersControllerIT extends BaseRestIntegrationTest {

    private static final String USER_API_ENDPOINT = "/api/user";

    @Test
    public void testGetUsers() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
            .when()
            .get(USER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-users-response-schema.json"));
    }

    @Test
    public void testGetUsersNoAccess() {
        RestAssured.sessionId = getAuthSession(BASIC_USER);

        given().expect().statusCode(FORBIDDEN)
            .when()
            .get(USER_API_ENDPOINT);
    }

    @Test
    public void testCreateUpdateDeleteUserNoAccess() {
        RestAssured.sessionId = getAuthSession(BASIC_USER);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());

        given()
            .contentType(ContentType.JSON)
            .body(userDTO)
            .expect().statusCode(FORBIDDEN)
            .when()
            .put(USER_API_ENDPOINT);

        given()
            .contentType(ContentType.JSON)
            .body(userDTO)
            .expect().statusCode(FORBIDDEN)
            .when()
            .post(USER_API_ENDPOINT + "/" + userDTO.getId());

        given()
            .expect().statusCode(FORBIDDEN)
            .when()
            .delete(USER_API_ENDPOINT + "/" + userDTO.getId());
    }

    @Test
    public void testCreateUpdateDeleteUser() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        LocalDateTime today = LocalDateTime.now();
        Integer totalElementsBefore = given().expect().statusCode(OK)
            .when()
            .get(USER_API_ENDPOINT)
            .then()
            .extract()
            .path("totalElements");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeHsaId("hsaId-1");
        userDTO.setIntygsadminRole(IntygsadminRole.FULL);
        userDTO.setName("name");

        String userId = given()
            .contentType(ContentType.JSON)
            .body(userDTO)
            .expect().statusCode(OK)
            .when()
            .put(USER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/put-user-response-schema.json"))
            .extract()
            .path("id");

        given().expect().statusCode(OK)
            .when()
            .get(USER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-users-response-schema.json"))
            .body("totalElements", is(totalElementsBefore + 1))
            .body("content.find { it.id == '" + userId + "' }.name",
                equalTo("name"));

        // Update
        userDTO.setName("New name");

        given()
            .contentType(ContentType.JSON)
            .body(userDTO)
            .expect().statusCode(OK)
            .when()
            .post(USER_API_ENDPOINT + "/" + userId)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/put-user-response-schema.json"));

        given().expect().statusCode(OK)
            .when()
            .get(USER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-users-response-schema.json"))
            .body("totalElements", is(totalElementsBefore + 1))
            .body("content.find { it.id == '" + userId + "' }.name",
                equalTo("New name"));

        // Delete
        given()
            .contentType(ContentType.JSON)
            .expect().statusCode(OK)
            .when()
            .delete(USER_API_ENDPOINT + "/" + userId);

        given().expect().statusCode(OK)
            .when()
            .get(USER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-users-response-schema.json"))
            .body("totalElements", is(totalElementsBefore));
    }

}
