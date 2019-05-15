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

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.persistence.enums.BannerPriority;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BannerControllerIT extends BaseRestIntegrationTest {

    private static final String BANNER_API_ENDPOINT = "/api/banner";

    @Test
    public void testGetBanners() {
        given().expect().statusCode(OK)
                .when()
                .get(BANNER_API_ENDPOINT)
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-response-schema.json"));
    }

    @Test
    public void testCreatBanner() {
        LocalDateTime today = LocalDateTime.now();
        Integer totalElementsBefore  = given().expect().statusCode(OK)
                .when()
                .get(BANNER_API_ENDPOINT)
                .then()
                .extract()
                    .path("totalElements");

        BannerDTO bannerDTO = new BannerDTO();

        bannerDTO.setMessage("hej");
        bannerDTO.setApplication(Application.WEBCERT);
        bannerDTO.setPriority(BannerPriority.HIGH);
        bannerDTO.setDisplayFrom(today.minusDays(10));
        bannerDTO.setDisplayTo(today.plusDays(10));

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
                .body("totalElements", is(totalElementsBefore + 1))
                .body("content.find { it.id == '" + bannerId + "' }.message",
                        equalTo("hej"));
    }

}
