package se.inera.intyg.intygsadmin.web;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBodyExtractionOptions;

import static io.restassured.RestAssured.given;

public abstract class BaseRestIntegrationTest {

    public static final int OK = HttpStatus.OK.value();
    public static final int NOT_FOUND = HttpStatus.NOT_FOUND.value();
    public static final int SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final int FORBIDDEN = HttpStatus.FORBIDDEN.value();


    /**
     * Common setup for all tests.
     */
    @BeforeEach
    public void setup() {
        RestAssured.reset();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = System.getProperty("integration.tests.baseUrl", "http://localhost:8680");
    }

    protected String loadJson(String filePath) {
        ClassPathResource cpr = new ClassPathResource(filePath);
        try {
            return IOUtils.toString(cpr.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Integer createBestallning(String json) {
        ResponseBodyExtractionOptions body = given().body(json).when().contentType("application/json")
                .post("/api/test/bestallningar").then()
                .statusCode(200).extract().body();
        return body.jsonPath().get("entity.id");
    }

    protected void deleteBestallning(Integer id) {
        given().when()
                .delete("/api/test/bestallningar/" + id)
                .then().statusCode(200);
    }
}
