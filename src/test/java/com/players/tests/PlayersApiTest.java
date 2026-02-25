package com.players.tests;

import com.players.api.AuthApi;
import com.players.config.ConfigReader;
import com.players.models.request.CreatePlayerRequest;
import com.players.models.request.GetOneRequest;
import com.players.models.request.LoginRequest;
import com.players.models.response.LoginResponse;
import com.players.models.response.PlayerResponse;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class PlayersApiTest extends BaseTest {

    private static CreatePlayerRequest firstPlayerRequest;

    @Test(priority = 1)
    public void test1_Login() {
        LoginRequest credentials = ConfigReader.getInstance().getCredentials();
        AuthApi authApi = new AuthApi();
        LoginResponse response = authApi.login(credentials);
        assertNotNull(response.getAccessToken());
        assertFalse(response.getAccessToken().isEmpty());
    }

    @Test(priority = 2)
    public void test2_CreateTwelvePlayers() {
        List<CreatePlayerRequest> players = ConfigReader.getInstance().getPlayers();
        assertEquals(players.size(), 12);
        firstPlayerRequest = players.get(0);

        for (CreatePlayerRequest request : players) {
            PlayerResponse response = getApi().createPlayer(request);
            assertNotNull(response.getUsername());
            assertNotNull(response.getEmail());
            assertNotNull(response.getName());
            assertNotNull(response.getSurname());
            assertEquals(response.getUsername(), request.getUsername());
            assertEquals(response.getEmail(), request.getEmail());
            assertEquals(response.getName(), request.getName());
            assertEquals(response.getSurname(), request.getSurname());
        }
    }

    @Test(priority = 3)
    public void test3_GetOnePlayer() {
        GetOneRequest request = GetOneRequest.builder()
                .email(firstPlayerRequest.getEmail())
                .build();

        PlayerResponse response = getApi().getPlayerByEmail(request);
        assertEquals(response.getEmail(), firstPlayerRequest.getEmail());
        assertEquals(response.getName(), firstPlayerRequest.getName());
        assertEquals(response.getSurname(), firstPlayerRequest.getSurname());
        assertEquals(response.getUsername(), firstPlayerRequest.getUsername());
    }

    @Test(priority = 4)
    public void test4_GetAllAndSortByName() {
        List<PlayerResponse> allPlayers = getApi().getAllPlayers();
        assertTrue(allPlayers.size() >= 12);

        createdPlayerIds.clear();
        for (PlayerResponse p : allPlayers) {
            if (p.getId() != null) {
                createdPlayerIds.add(p.getId());
            }
        }

        List<String> names = allPlayers.stream()
                .map(PlayerResponse::getName)
                .filter(n -> n != null)
                .collect(Collectors.toList());

        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(String.CASE_INSENSITIVE_ORDER);

        for (int i = 0; i < sortedNames.size() - 1; i++) {
            assertTrue(
                    String.CASE_INSENSITIVE_ORDER.compare(sortedNames.get(i), sortedNames.get(i + 1)) <= 0,
                    "Names not sorted at index " + i + ": " + sortedNames.get(i) + " > " + sortedNames.get(i + 1));
        }
        assertFalse(sortedNames.isEmpty());
    }

    @Test(priority = 5)
    public void test5_DeleteAllCreatedPlayers() {
        assertFalse(createdPlayerIds.isEmpty());
        for (String id : createdPlayerIds) {
            getApi().deletePlayer(id);
        }
    }

    @Test(priority = 6)
    public void test6_VerifyListIsEmpty() {
        List<PlayerResponse> allPlayers = getApi().getAllPlayers();
        assertTrue(allPlayers.isEmpty());
    }
}
