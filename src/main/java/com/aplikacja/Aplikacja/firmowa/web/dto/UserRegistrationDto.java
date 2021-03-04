package com.aplikacja.Aplikacja.firmowa.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDto {
    private String first_name;
    private String last_name;
    private String email;
    private String password;

}
