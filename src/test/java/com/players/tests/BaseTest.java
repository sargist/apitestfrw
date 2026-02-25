package com.players.tests;

import com.players.api.AuthApi;
import com.players.api.PlayersApi;
import com.players.config.ConfigReader;
import com.players.models.request.LoginRequest;
import com.players.models.response.LoginResponse;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseTest {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    protected static final CopyOnWriteArrayList<String> createdPlayerIds = new CopyOnWriteArrayList<>();

    @BeforeClass
    public void setUp() {
        ConfigReader config = ConfigReader.getInstance();
        LoginRequest credentials = config.getCredentials();
        AuthApi authApi = new AuthApi();
        LoginResponse loginResponse = authApi.login(credentials);
        tokenHolder.set(loginResponse.getAccessToken());
    }

    /** Returns a PlayersApi instance authenticated with the current token. */
    protected PlayersApi getApi() {
        return new PlayersApi(tokenHolder.get());
    }

    /** Returns the current authentication token. */
    protected String getToken() {
        return tokenHolder.get();
    }
}
