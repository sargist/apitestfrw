package com.players.api;

import com.players.config.ConfigReader;
import com.players.models.request.LoginRequest;
import com.players.models.response.LoginResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthApi {

    private final String baseUrl;
    private final String basicAuth;

    public AuthApi() {
        this.baseUrl = ConfigReader.getInstance().getBaseUrl();
        this.basicAuth = ConfigReader.getInstance().getBasicAuth();
    }

    /** Authenticates and returns login response with token. */
    public LoginResponse login(LoginRequest request) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .header("Authorization", "Basic " + basicAuth)
                .body(request)
                .when()
                .post("/api/tester/login")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(LoginResponse.class);
    }
}
