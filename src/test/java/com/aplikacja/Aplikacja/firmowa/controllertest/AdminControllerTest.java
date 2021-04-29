package com.aplikacja.Aplikacja.firmowa.controllertest;

import org.springframework.boot.test.context.SpringBootTest;
import com.aplikacja.Aplikacja.firmowa.dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.services.exceptions.UserNotExistException;
import com.aplikacja.Aplikacja.firmowa.security.jwebtoken.JWebTokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWebTokenUtils jWebTokenUtils;

    @Test
    void createNewuserAccount()throws Exception{
        //Given
        String login = "TestNewUserAccount";
        String password = "testUserAccountPassword";
        Set<String> roles = new HashSet<>();
        roles.add("user");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(login, "Test",
                "User", "testusermail@dot.com", roles,
                password)
                , headers);
        //When
        ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("admin",""),
                HttpMethod.POST, entity, Object.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("New user account created successfully!", response.getBody());
        //CleanUp
        String userId = Long.toString(userRepository
                .findByLogin(login)
                .orElseThrow(() -> new UserNotExistException(0L))
                .getId());
        removeUser(userId, login, password);
    }

    @Test
    void testGetUser()throws Exception {
        //Given
        String login = "loginForGetUser";
        String firstName = "Jan";
        String lastName = "Nowak";
        String email = "nowakjan@testjan.com";
        String role = "user";
        String password = "testgetuserpassword";
        String userId = createUser(login, firstName, lastName, email, role, password);
        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("admin","" + userId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(firstName, lastName, email)));
        //CleanUp
        removeUser(userId, login, password);
    }

    @Test
    void testGetAll() throws Exception {
        //Given
        String user1login = "testgetalllogin1";
        String user1firstName = "Adam";
        String user1lastName = "Cesar";
        String user1email = "test.getall@mail.com";
        String role1 = "user";
        String user1password = "testgetalluser1pass";
        String user2login = "testgetAlluser2";
        String user2firstName = "Leticia";
        String user2lastName = "Gracia";
        String user2email = "testGetAll@email2.com";
        String role2 = "user";
        String user2password = "testgetalluser2pass";
        String userId1 = createUser(user1login, user1firstName, user1lastName, user1email, role1, user1password);
        String userId2 = createUser(user2login, user2firstName, user2lastName, user2email, role2, user2password);
        //When
        HttpHeaders headers = createHttpHeaders(user1login, user1password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("admin",""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createUserJsonData(user1firstName, user1lastName, user1email)));
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createUserJsonData(user2firstName, user2lastName, user2email)));
        //CleanUp
        removeUser(userId1, user1login, user1password);
        removeUser(userId2, user2login, user2password);
    }

    @Test
    void testDeleteUserAccount() throws Exception {
        //Given
        String login = "sweetcat";
        String firstName = "Hannah";
        String lastName = "Montana";
        String email = "testhanahmail@hanah.com";
        String role = "user";
        String password = "hanahPassword";
        String userId = createUser(login, firstName, lastName, email, role, password);
        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("admin","" + userId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUserData() throws Exception{
        //Given
        String login = "paulsLogin";
        String firstName = "Paul";
        String lastName = "Gonzalez";
        String email = "paul@mail.com";
        String role = "user";
        String password = "paulPass";
        String userId = createUser(login, firstName, lastName, email, role, password);
        String changedName = "Kirito";
        Set<String> roles = new HashSet<>();
        roles.add("user");
        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(firstName, changedName,
                lastName, email, roles, password), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("admin","" + userId),
                HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createUserJsonData(changedName, lastName, email)));
        //CleanUp
        removeUser(userId, login, password);
    }

    private String createURLWithPort(String controller,String uri) {
        return "http://localhost:" + port + "/" + controller + "/" + uri;
    }

    private String createUserJsonData(String firstName, String lastName, String email) {
        return "\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"email\":\"" + email;
    }

    private String createUser(String login, String firstName, String lastName, String email, String role, String password) {
        Set<String> roles = new HashSet<>();
        roles.add(role);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(login, firstName, lastName, email, roles, password),
                headers);
        restTemplate.exchange(createURLWithPort("admin",""),
                HttpMethod.POST, entity, String.class);
        return Long.toString(userRepository
                .findByLogin(login)
                .orElseThrow(() -> new UserNotExistException(0L))
                .getId());
    }

    private void removeUser(String id, String login, String password) {
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<UserDto> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("admin","" + id),
                HttpMethod.DELETE, entity, Object.class);
    }

    private HttpHeaders createHttpHeaders(String login, String password) {
        String encodedAuth = "Bearer  " + generateJwt(login, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", encodedAuth);
        return headers;
    }

    private String generateJwt(String login, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication = authManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jWebTokenUtils.generateJWebToken(authentication);
    }
}
