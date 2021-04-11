package com.aplikacja.Aplikacja.firmowa.ControllerTests;

import com.aplikacja.Aplikacja.firmowa.Dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.UserNotExistException;
import com.aplikacja.Aplikacja.firmowa.security.JWebToken.JWebTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;


import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAndAdminControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWebTokenUtils jWebTokenUtils;

    @Test
    void testNewUser() {
        //Given
        String login = "login";
        String password = "passwordtotests";
        Set<String> roles = new HashSet<>();
        roles.add("user");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(new UserDto(login, "Tomasz",
                "Surname", "testowymail@dot.com", roles,
                password)
                , headers);

        //When
        ResponseEntity<?> responseEntity = testRestTemplate.exchange(createURLWithPort
                        ("authorize/register"),
                HttpMethod.POST, httpEntity, Object.class);

        //Then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotEquals("Account created", responseEntity.getBody());

        //CleanUp
        String userId = Long.toString(userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotExistException(0L)).getId());
        removeUser(userId, login, password);
    }

    @Test
    void testGettingUser() {
        //Given
        String login = "loginToTestGettingUser";
        String firstName = "Test";
        String lastName = "User";
        String email = "testowymail@o2.pl";
        String role = "admin";
        String password = "testowehaslodlausera";
        String userId = createNewUser(login, firstName, lastName, email, role, password);

        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("" + userId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createNewUserJsonData(firstName, lastName, email)));
        //CleanUp
        removeUser(userId, login, password);
    }

    @Test
    void testGetAllUsers() {
        //Given
        String firstUserLogin = "firstUserLoginTest";
        String firstUserFirstName = "Adam";
        String firstUserLastName = "Kowalik";
        String firstUserEmail = "getfirstuser@mail.pl";
        String firstUserRole = "admin";
        String firstUserPassword = "firstUserTestPassword";

        String secondUserLogin = "secondUserLoginTest";
        String secondUserFirstName = "Ewelina";
        String secondUserLastName = "Michnik";
        String secondUserEmail = "getseconduser@mail.pl";
        String secondUserRole = "user";
        String secondUserPassword = "secondUserTestPassword";

        String firstUserId = createNewUser(firstUserLogin, firstUserFirstName, firstUserLastName,
                firstUserEmail, firstUserRole, firstUserPassword);

        String secondUserId = createNewUser(secondUserLogin, secondUserFirstName, secondUserLastName,
                secondUserEmail, secondUserRole, secondUserPassword);

        //When
        HttpHeaders headers = createHttpHeaders(firstUserLogin, firstUserPassword);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort(""),
                HttpMethod.GET, entity, String.class);

        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createNewUserJsonData(firstUserFirstName, firstUserLastName, firstUserEmail)));
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createNewUserJsonData(secondUserFirstName, secondUserLastName, secondUserEmail)));

        //CleanUp
        removeUser(firstUserId, firstUserLogin, firstUserPassword);
        removeUser(secondUserId, secondUserLogin, secondUserPassword);
    }

    @Test
    void DeleteUserTest() {
        //Given
        String login = "testLoginToDelete";
        String firstName = "Anna";
        String lastName = "Ramirez";
        String email = "testdelete@mail.pl";
        String role = "user";
        String password = "examplePassword";
        String userId = createNewUser(login, firstName, lastName, email, role, password);

        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort(""),
                HttpMethod.DELETE, entity, String.class);

        //Then
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUserData() {
        //Given
        String login = " Adamos";
        String firstName = "Adam";
        String lastName = "Ramirez";
        String email = "adamramirez@mail.pl";
        String role = "user";
        String password = "testPasswordToChange";
        String userId = createNewUser(login, firstName, lastName, email, role, password);
        String changeFirstName = "Edvard";
        Set<String> roles = new HashSet<>();
        roles.add("user");

        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(login, changeFirstName, lastName,
                email, roles, password), headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort(""
                        + userId),
                HttpMethod.PUT, entity, String.class);

        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createNewUserJsonData(changeFirstName, lastName, email)));

        //CleanUp
        removeUser(userId, login, password);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/user/" + uri;
    }

    private String createNewUserJsonData(String firstName, String lastName, String email) {
        return "\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName
                + "\",\"email\":\"" + email;
    }

    private String createNewUser(String login, String firstName, String lastName, String email,
                                 String role, String password) {
        Set<String> roles = new HashSet<>();
        roles.add(role);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(login, firstName, lastName, email,
                roles, password), headers);
        testRestTemplate.exchange(createURLWithPort("authorize/register"),
                HttpMethod.POST, entity, String.class);
        return Long.toString(userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotExistException(0L))
                .getId());
    }

    private void removeUser(String id, String login, String password) {
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        testRestTemplate.exchange(createURLWithPort("" + id),
                HttpMethod.DELETE, entity, Object.class);
    }

    private HttpHeaders createHttpHeaders(String login, String password) {
        String encodedAuthorize = "Bearer  " + generateJWebToken(login, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorized", encodedAuthorize);
        return headers;
    }

    private String generateJWebToken(String login, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication =
                authManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jWebTokenUtils.generateJWebToken(authentication);
    }

}




