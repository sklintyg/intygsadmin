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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.restassured.response.ResponseBody;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;

import static io.restassured.RestAssured.given;

public class BannerControllerIT extends BaseRestIntegrationTest {

    private static final String BANNER_API_ENDPOINT = "/api/banner";

    @Test
    public void testGetBanners() {
        ResponseBody responseBody = given().expect().statusCode(OK)
                .when()
                .get(BANNER_API_ENDPOINT)
                .getBody();
    }

    @Test
    @Disabled
    public void testGetBanner() {


        ResponseBody created = given().expect().statusCode(OK)
                .when()
                .put(BANNER_API_ENDPOINT)
                .getBody();


        ResponseBody responseBody = given().expect().statusCode(OK)
                .when()
                .get(BANNER_API_ENDPOINT + "/1")
                .getBody();
    }

}
