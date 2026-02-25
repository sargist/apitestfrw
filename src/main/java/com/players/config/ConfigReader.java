package com.players.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.players.models.request.CreatePlayerRequest;
import com.players.models.request.LoginRequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfigReader {

    private static ConfigReader instance;
    private final JsonNode root;
    private final String runId;

    private ConfigReader() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("testdata.json");
            root = mapper.readTree(is);
            runId = UUID.randomUUID().toString().substring(0, 8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read testdata.json", e);
        }
    }

    /** Returns the singleton instance. */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /** Returns the base URL from test data. */
    public String getBaseUrl() {
        return root.get("base_url").asText();
    }

    /** Returns the Basic Auth token. */
    public String getBasicAuth() {
        return root.get("basic_auth").asText();
    }

    /** Returns tester login credentials. */
    public LoginRequest getCredentials() {
        JsonNode creds = root.get("credentials");
        return LoginRequest.builder()
                .email(creds.get("email").asText())
                .password(creds.get("password").asText())
                .build();
    }

    /** Returns all player test data objects with unique email/username per run. */
    public List<CreatePlayerRequest> getPlayers() {
        List<CreatePlayerRequest> players = new ArrayList<>();
        int index = 0;
        for (JsonNode node : root.get("players")) {
            index++;
            String uniqueEmail = runId + ".p" + index + "@test.com";
            String uniqueUsername = node.get("username").asText() + "_" + runId;
            players.add(CreatePlayerRequest.builder()
                    .currency_code(node.get("currency_code").asText())
                    .email(uniqueEmail)
                    .name(node.get("name").asText())
                    .password_change(node.get("password_change").asText())
                    .password_repeat(node.get("password_repeat").asText())
                    .surname(node.get("surname").asText())
                    .username(uniqueUsername)
                    .build());
        }
        return players;
    }
}
