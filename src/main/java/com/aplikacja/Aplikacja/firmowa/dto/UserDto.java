package com.aplikacja.Aplikacja.firmowa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

public class UserDto {

    @NotBlank
    private String login;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    private String password;

    private LocalDateTime signUpDate;

    public UserDto(@NotBlank @Size(min = 3, max = 20) String login,
                   @NotBlank @Size(min = 3, max = 20) String firstName,
                   @NotBlank @Size(min = 3, max = 20) String lastName,
                   @NotBlank @Size(max = 50)
                   @Email String email, Set<String> role,
                   @NotBlank @Size(min = 6, max = 40) String password) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
    }

}
