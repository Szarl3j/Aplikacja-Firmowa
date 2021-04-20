package com.aplikacja.Aplikacja.firmowa.Load.requestloginsystem.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JWebTokenResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String login;
    private String email;
    private List<String> role;

    public JWebTokenResponse(String accessToken, Long id, String login, String email, List<String> role) {
        this.token = accessToken;
        this.id = id;
        this.login = login;
        this.email = email;
        this.role = role;
    }
}
