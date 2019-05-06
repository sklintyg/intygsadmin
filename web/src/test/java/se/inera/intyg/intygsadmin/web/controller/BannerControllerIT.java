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
