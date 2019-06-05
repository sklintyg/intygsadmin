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

package se.inera.intyg.intygsadmin.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import se.inera.intyg.intygsadmin.web.auth.fake.FakeUser;

import javax.servlet.http.HttpServletResponse;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_LOGIN_ENDPOINT;

public abstract class BaseRestIntegrationTest {

    public static final int OK = HttpStatus.OK.value();
    public static final int NOT_FOUND = HttpStatus.NOT_FOUND.value();
    public static final int SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final int FORBIDDEN = HttpStatus.FORBIDDEN.value();

    private static final String USER_JSON_FORM_PARAMETER = "userJsonDisplay";

    protected final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    protected String baseUrl = System.getProperty("integration.tests.baseUrl", "http://localhost:8680");

    protected static final FakeUser ADMIN_USER = new FakeUser(
            "TSTNMT2321000156-10KK",
            "ADMIN",
            "Karl Nilsson");
    protected static final FakeUser BASIC_USER = new FakeUser(
            "TSTNMT2321000156-10KL",
            "BASIC",
            "Lena Svensson");

    /**
     * Common setup for all tests.
     */
    @BeforeEach
    public void setup() {
        RestAssured.reset();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = baseUrl;
    }

    protected String getAuthSession(FakeUser fakeUser) {
        String credentialsJson;
        try {
            credentialsJson = objectMapper.writeValueAsString(fakeUser);
            return getAuthSession(credentialsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAuthSession(String credentialsJson) {
        Response response = given().contentType(ContentType.URLENC).and().redirects().follow(false).and()
                .formParam(USER_JSON_FORM_PARAMETER, credentialsJson).expect()
                .statusCode(HttpServletResponse.SC_FOUND).when()
                .post(FAKE_LOGIN_ENDPOINT).then().extract().response();
        assertNotNull(response.sessionId());
        return response.sessionId();
    }
}
