package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Services.LogsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

public class ContentIT extends BaseIntegrationTest{
    @LocalServerPort
    private int port;

    @MockBean
    private LogsService logsService;

    @MockBean

    @Autowired
    private TestRestTemplate restTemplate;

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeEach
    void setup() {
        String username = "admin";
        String password = "admin";
        String requestBody = "{\"username\":\"" + "admin" + "\",\"password\":\"" + "admin" + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate1.exchange(
                "http://localhost:"+ port + "/api/login",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(200, response.getStatusCodeValue());

        String responseBody = response.getBody();

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void testGetAllContent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Content>> response = restTemplate.exchange("http://localhost:" + port + "/content", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Content>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @Order(2)
    void testGetContentById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Content> response = restTemplate.exchange("http://localhost:" + port + "/content/1", HttpMethod.GET, requestEntity, Content.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("station", response.getBody().getName());
        assertEquals("options", response.getBody().getType());
    }

    @Test
    @Order(3)
    void testSaveContent(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        ResponseEntity<Widget> responseWidget = restTemplate.exchange("http://localhost:" + port + "/widget/1", HttpMethod.GET, new HttpEntity<>(headers), Widget.class);
        Widget widget = responseWidget.getBody();

        Content content = new Content();
        content.setName("Test content");
        content.setType("test");
        content.setOptions(List.of("test1", "test2", "test3"));
        content.setWidget(widget);

        HttpEntity<Content> requestEntity = new HttpEntity<>(content, headers);

        ResponseEntity<Content> response = restTemplate.exchange("http://localhost:" + port + "/content", HttpMethod.POST, requestEntity, Content.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test content", response.getBody().getName());
        assertEquals(3, response.getBody().getOptions().size());
        assertEquals("test", response.getBody().getType());
    }

    @Test
    @Order(4)
    void testUpdateContent(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Content> response = restTemplate.exchange("http://localhost:" + port + "/content/3", HttpMethod.GET, requestEntity, Content.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Content content = response.getBody();
        content.setName("Updated content");
        content.setType("updated");

        HttpEntity<Content> requestEntity2 = new HttpEntity<>(content, headers);

        ResponseEntity<Content> response2 = restTemplate.exchange("http://localhost:" + port + "/content/1", HttpMethod.PUT, requestEntity2, Content.class);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals("Updated content", response2.getBody().getName());
    }

    @Test
    @Order(5)
    void testDeleteContent(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Content> response = restTemplate.exchange("http://localhost:" + port + "/content/1", HttpMethod.DELETE, requestEntity, Content.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<Content> response2 = restTemplate.exchange("http://localhost:" + port + "/content/1", HttpMethod.GET, requestEntity, Content.class);

        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertNull(response2.getBody());
    }






}
