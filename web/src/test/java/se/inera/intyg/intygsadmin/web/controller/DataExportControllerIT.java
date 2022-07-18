/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsadmin.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;

public class DataExportControllerIT extends BaseRestIntegrationTest {

    private static final String API_DATAEXPORT = "/api/dataExport" ;

    @Test
    public void shouldFetchSortedTerminations() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        final var pageDescending = given()
            .when().get(API_DATAEXPORT)
            .then().statusCode(OK).extract().body().jsonPath().getList("content", DataExportResponse.class);

        final var pageAscending = given()
            .queryParam("page", "0")
            .queryParam("size", 10)
            .queryParam("sort", "createdAt,ASC")
            .when().get(API_DATAEXPORT)
            .then().statusCode(OK).extract().body().jsonPath().getList("content", DataExportResponse.class);

        assertAll(
            () -> assertTrue(pageDescending.size() > 1),
            () -> assertTrue(pageAscending.size() > 1),
            () -> assertNotEquals(pageDescending.get(0), pageAscending.get(0)),
            () -> assertNotEquals(pageDescending.get(1), pageAscending.get(1))
        );
    }

    @Test
    public void shouldFetchPagedTerminations() {
        RestAssured.sessionId = getAuthSession(ADMIN_USER);

        final var page1 = given()
            .queryParam("page", "0")
            .queryParam("size", 1)
            .when().get(API_DATAEXPORT)
            .then().statusCode(OK).extract().body().jsonPath().getList("content", DataExportResponse.class);

        final var page2 = given()
            .queryParam("page", "1")
            .queryParam("size", 1)
            .when().get(API_DATAEXPORT)
            .then().statusCode(OK).extract().body().jsonPath().getList("content", DataExportResponse.class);

        assertAll(
            () -> assertEquals(1, page1.size()),
            () -> assertEquals(1, page2.size()),
            () -> assertNotEquals(page1.get(0), page2.get(0))
        );
    }
}
