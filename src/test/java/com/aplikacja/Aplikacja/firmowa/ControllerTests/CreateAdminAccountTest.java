package com.aplikacja.Aplikacja.firmowa.ControllerTests;

import com.aplikacja.Aplikacja.firmowa.Dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.UserNotExistException;
import com.aplikacja.Aplikacja.firmowa.security.JWebToken.JWebTokenUtils;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;



//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateAdminAccountTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWebTokenUtils jwtUtils;

    @Test
    void testCreateAdminAccount() {
        //Given
        String login = "Admin";
        String password = "testAdminAccountPasswordAbc";
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(login, "Tomasz",
                "Moch", "testadminmailtomasz@dot.com", roles,
                password)
                , headers);
        //When
        ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("authorize/register"),
                HttpMethod.POST, entity, Object.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("Admin account created successfully!", response.getBody());
        //CleanUp
        String userId = Long.toString(userRepository
                .findByLogin(login)
                .orElseThrow(() -> new UserNotExistException(0L))
                .getId());
        removeUser(userId, login, password);

        // konto admina tworzy się bez problemu. Żeby test przechodził za każdym razem trzeba pozmieniać wszystkie dane.
        // w przeciwnym wypadku pojawia się błąd 400. jednym słowem nowy użytkownik ma unikalne dane.
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/user/" + uri;
    }

    private String createUserJsonData(String name, String surname, String email) {
        return "\"name\":\"" + name + "\",\"surname\":\"" + surname + "\",\"email\":\"" + email;
    }

    private String createUser(String username, String name, String surname, String email, String role, String password) {
        Set<String> roles = new HashSet<>();
        roles.add(role);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(username, name, surname, email, roles, password),
                headers);
        restTemplate.exchange(createURLWithPort("auth/signup"),
                HttpMethod.POST, entity, String.class);
        return Long.toString(userRepository
                .findByLogin(username)
                .orElseThrow(() -> new UserNotExistException(0L))
                .getId());
    }

    private void removeUser(String id, String username, String password) {
        HttpHeaders headers = createHttpHeaders(username, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("" + id),
                HttpMethod.DELETE, entity, Object.class);
    }

    private HttpHeaders createHttpHeaders(String username, String password) {
        String encodedAuth = "Bearer  " + generateJwt(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", encodedAuth);
        return headers;
    }

    private String generateJwt(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJWebToken(authentication);
    }
}