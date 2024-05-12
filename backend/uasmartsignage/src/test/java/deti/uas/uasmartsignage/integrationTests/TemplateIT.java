package deti.uas.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import deti.uas.uasmartsignage.Models.Template;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateIT  extends BaseIntegrationTest{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeEach
    void setup() {
        String username = "admin";
        String password = "admin";
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

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
    void testGetAllTemplates(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Template>> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Template>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size()); //could change if any template is added to DataInit
    }

    @Test
    @Order(2)
    void testGetTemplateById(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/1", HttpMethod.GET, requestEntity, Template.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("template1", response.getBody().getName());

    }

    @Test
    @Order(3)
    void testSaveTemplate(){//will have some changes
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Template template = new Template();
        template.setName("template4");

        HttpEntity<Template> requestEntity = new HttpEntity<>(template, headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates", HttpMethod.POST, requestEntity, Template.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("template4", response.getBody().getName());
    }

    @Test
    @Order(4)
    void testUpdateTemplate(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Template> requestEntity = new HttpEntity<>( headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/3", HttpMethod.GET, requestEntity, Template.class);
        Template template = response.getBody();

        template.setName("updatedTemplate");

        HttpEntity<Template> updateentity = new HttpEntity<>(template, headers);

        ResponseEntity<Template> response2 = restTemplate.exchange("http://localhost:"+ port + "/api/templates/3", HttpMethod.PUT, updateentity, Template.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals("updatedTemplate", response2.getBody().getName());



    }

    @Test
    @Order(5)
    void testDeleteTemplate(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/3", HttpMethod.DELETE, requestEntity, Template.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); }
}
