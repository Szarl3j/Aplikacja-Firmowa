package com.aplikacja.Aplikacja.firmowa.Load.RequestLogin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RequiredLogin {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
