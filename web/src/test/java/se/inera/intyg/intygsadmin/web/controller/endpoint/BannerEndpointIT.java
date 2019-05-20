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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BannerEndpointIT extends BaseRestIntegrationTest {

    private static final String BANNER_ACTUATOR_ENDPOINT = "/actuator/banner";

    public BannerEndpointIT() {
        baseUrl = System.getProperty("integration.tests.actuatorUrl", "http://localhost:8681");
    }

    @Test
    public void testGetBannersForWebcert() {
        given().expect().statusCode(OK)
                .when()
                .get(BANNER_ACTUATOR_ENDPOINT + "/" + Application.WEBCERT)
                .then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-banners-actuator-response-schema.json"));
    }

}
