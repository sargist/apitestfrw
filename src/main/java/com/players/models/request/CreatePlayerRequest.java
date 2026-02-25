package com.players.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerRequest {
    private String currency_code;
    private String email;
    private String name;
    private String password_change;
    private String password_repeat;
    private String surname;
    private String username;
}
