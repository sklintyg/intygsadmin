/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
import org.junit.jupiter.api.Test;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.infra.driftbannerdto.BannerPriority;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;

public class BannerControllerIT extends BaseRestIntegrationTest {

    private static final String BANNER_API_ENDPOINT = "/api/banner";

    @Test
    public void testGetBanners() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
            .when()
            .get(BANNER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-response-schema.json"));
    }

    @Test
    public void testGetActiveAnfFutureBanners() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        given().expect().statusCode(OK)
            .when()
            .get(BANNER_API_ENDPOINT + "/activeAndFuture?application=" + Application.WEBCERT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-actuator-response-schema.json"));
    }

    @Test
    public void testCreateUpdateDeleteBanner() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        LocalDateTime today = LocalDateTime.now();
        Integer totalElementsBefore = given().expect().statusCode(OK)
            .when()
            .get(BANNER_API_ENDPOINT)
            .then()
            .extract()
            .path("page.totalElements");

        BannerDTO bannerDTO = new BannerDTO();

        bannerDTO.setMessage("hej");
        bannerDTO.setApplication(Application.WEBCERT);
        bannerDTO.setPriority(BannerPriority.HOG);
        bannerDTO.setDisplayFrom(today.plusDays(200));
        bannerDTO.setDisplayTo(today.plusDays(210));

        String bannerId = given()
            .contentType(ContentType.JSON)
            .body(bannerDTO)
            .expect().statusCode(OK)
            .when()
            .put(BANNER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/put-banner-response-schema.json"))
            .extract()
            .path("id");

        given().expect().statusCode(OK)
            .when()
            .get(BANNER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-response-schema.json"))
            .body("page.totalElements", is(totalElementsBefore + 1))
            .body("content.find { it.id == '" + bannerId + "' }.message",
                equalTo("hej"));

        // Update
        bannerDTO.setMessage("New message");

        given()
            .contentType(ContentType.JSON)
            .body(bannerDTO)
            .expect().statusCode(OK)
            .when()
            .post(BANNER_API_ENDPOINT + "/" + bannerId)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/put-banner-response-schema.json"));

        given().expect().statusCode(OK)
            .when()
            .get(BANNER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-response-schema.json"))
            .body("page.totalElements", is(totalElementsBefore + 1))
            .body("content.find { it.id == '" + bannerId + "' }.message",
                equalTo("New message"));

        // Delete
        given()
            .contentType(ContentType.JSON)
            .expect().statusCode(OK)
            .when()
            .delete(BANNER_API_ENDPOINT + "/" + bannerId);

        given().expect().statusCode(OK)
            .when()
            .get(BANNER_API_ENDPOINT)
            .then()
            .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-response-schema.json"))
            .body("page.totalElements", is(totalElementsBefore));
    }

}
