package com.players.api;

import com.players.config.ConfigReader;
import com.players.models.request.CreatePlayerRequest;
import com.players.models.request.GetOneRequest;
import com.players.models.response.PlayerResponse;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PlayersApi {

    private final String baseUrl;
    private final String token;

    public PlayersApi(String token) {
        this.baseUrl = ConfigReader.getInstance().getBaseUrl();
        this.token = token;
    }

    private RequestSpecification baseRequest() {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token);
    }

    /** Creates a new player and returns player response. */
    public PlayerResponse createPlayer(CreatePlayerRequest request) {
        return baseRequest()
                .body(request)
                .when()
                .post("/api/automationTask/create")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PlayerResponse.class);
    }

    /** Gets a single player by email. */
    public PlayerResponse getPlayerByEmail(GetOneRequest request) {
        return baseRequest()
                .body(request)
                .when()
                .post("/api/automationTask/getOne")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(PlayerResponse.class);
    }

    /** Gets all players. */
    public List<PlayerResponse> getAllPlayers() {
        return baseRequest()
                .when()
                .get("/api/automationTask/getAll")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", PlayerResponse.class);
    }

    /** Deletes a player by ID. */
    public void deletePlayer(String id) {
        baseRequest()
                .when()
                .delete("/api/automationTask/deleteOne/" + id)
                .then()
                .assertThat()
                .statusCode(200);
    }
}
