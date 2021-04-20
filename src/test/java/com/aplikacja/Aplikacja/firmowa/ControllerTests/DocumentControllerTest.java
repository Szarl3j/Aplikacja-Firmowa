package com.aplikacja.Aplikacja.firmowa.ControllerTests;

import com.aplikacja.Aplikacja.firmowa.dto.DocumentDto;
import com.aplikacja.Aplikacja.firmowa.dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.services.exceptions.UserNotExistException;
import com.aplikacja.Aplikacja.firmowa.security.jwebtoken.JWebTokenUtils;
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
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "Admin", password ="testAdminAccountPasswordAbc", roles = "ADMIN_ROLE")
public class DocumentControllerTest {

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
    void testAddDocument() throws Exception {
        //Given
        String login = "usernameA";
        String password = "passwordA";
        String role= "admin";
        Long userId = createUser(login, password);
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(new DocumentDto("Pierwsze kroki",
                "Pierwsze kroki w tworzeniu aplikacji"), headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("document", ""),
                HttpMethod.POST, entity, String.class);
        String documentId = response.getBody();
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotEquals("0", documentId);
        //CleanUp
        removeDocument(documentId, login, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testGetDocument() throws Exception{
        //Given
        String login = "loginUser2";
        String password = "passwordUser2";
        Long userId = createUser(login, password);
        String title = "exampleTitle";
        String description = "descriptionForDocument";
        String documentId = createDocument( title,description,  login, password);
        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("document", "" + documentId),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createDocumentJsonData( title)));
        //CleanUp
        removeDocument(documentId, login, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testGetAllDocuments() throws Exception {
        //Given
        String login = "loginuser3";
        String password = "passworduser3";
        Long userId = createUser(login, password);
        String title1 = "GetAllDocumentTitle1";
        String description1= "description1";
        String title2 = "GetAllDocumentTitle2";
        String description2="description2";
        String documentId1 = createDocument( title1,description1,  login, password);
        String documentId2 = createDocument(title2,description2,  login, password);

        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(null, headers);
        //When
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("document", ""),
                HttpMethod.GET, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createDocumentJsonData( title1)));
        assertTrue(Objects.requireNonNull(response.getBody()).contains(createDocumentJsonData(title1)));
        //CleanUp
        removeDocument(documentId1, login, password);
        removeDocument(documentId2, login, password);
        userRepository.deleteById(userId);
    }

    @Test
    void testDeleteDocument() throws Exception{
        //Given
        String login = "usernameD";
        String password = "passwordD";
        Long userId = createUser(login, password);
        String documentId = createDocument("TitleDeleteBook", "DescriptionBookToDelete", login, password);
        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("document", "" + documentId),
                HttpMethod.DELETE, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        //CleanUp
        userRepository.deleteById(userId);
    }

    @Test
    void testUpdateDocumentData() throws Exception {
        //Given
        String login = "username5";
        String password = "password5";
        Long userId = createUser(login, password);
        String title = "TitleUpdate";
        String description="descriptionForTest";
        String updatedTitle = "NewTitle";
        String updateDescription= "newDescription";
        String documentId = createDocument( title,description , login, password);
        //When
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(new DocumentDto( updatedTitle,updateDescription), headers);
        ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("document", "" + documentId),
                        HttpMethod.PUT, entity, String.class);
        //Then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(response.getBody())
                .contains(createDocumentJsonData(updatedTitle)));
        //CleanUp
        removeDocument(documentId, login, password);
        userRepository.deleteById(userId);
    }

    private String createURLWithPort(String controller, String uri) {
        return "http://localhost:" + port + "/" + controller + "/" + uri;
    }

    private String createDocumentJsonData(String title) {
        return " \",\"title\":\"" + title + "\"";
    }

    private String createDocument( String title,String description,String login, String password) {
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(new DocumentDto( title, description), headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("document", ""),
                HttpMethod.POST, entity, String.class);
        System.out.println("Document created: "  + " " + title + ", "  + ", id: " + response.getBody());
        return response.getBody();
    }

    private void removeDocument(String id, String login, String password) {
        HttpHeaders headers = createHttpHeaders(login, password);
        HttpEntity<DocumentDto> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("document", "" + id),
                HttpMethod.DELETE, entity, String.class);
    }

    private Long createUser(String login, String password) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(new UserDto(login, login, login,
                login + "@dot.com", roles, password),
                headers);
        restTemplate.exchange(createURLWithPort("user", "authorize/register"),
                HttpMethod.POST, entity, String.class);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotExistException(0L))
                .getId();
    }

    private HttpHeaders createHttpHeaders(String login, String password) {
        String encodedAuth = "Bearer  " + generateJwt(login, password);
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
        return jWebTokenUtils.generateJWebToken(authentication);
    }
}
