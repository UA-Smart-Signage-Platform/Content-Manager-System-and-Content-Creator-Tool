package deti.uas.uasmartsignage.integrationTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import java.util.List;

class MonitorsGroupIT extends BaseIntegrationTest{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeEach
    void setup() {
        // Prepare the request body with valid credentials
        String username = "admin";
        String password = "admin";
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make a POST request to your authentication endpoint to get the JWT token
        ResponseEntity<String> response = restTemplate1.exchange(
                "http://localhost:"+ port + "/api/login",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //System.out.println("ertyuihgfdsadfgbhnjmkl.,kmjhngbf" + response.getBody());

        // Ensure that the request was successful (HTTP status code 200)
        assertEquals(200, response.getStatusCodeValue());

        // Extract the JWT token from the response body
        String responseBody = response.getBody();

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }



    @Test
    @Order(1)
    void testGetAllMonitorsGroups() {
        System.out.println("TestBomboclat" + port);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<MonitorsGroup>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.GET,
                entity, new ParameterizedTypeReference<List<MonitorsGroup>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    @Order(2)
    void testGetMonitorsGroupsThatAreNotAssociatedWithOnlyOneMonitor() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<MonitorsGroup>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/notMadeForMonitor", HttpMethod.GET,
                entity, new ParameterizedTypeReference<List<MonitorsGroup>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    @Order(3)
    void testGetGroupByIdEndpoint(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<MonitorsGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1", HttpMethod.GET,entity,MonitorsGroup.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("DETI", response.getBody().getName());
    }

    @Test
    @Order(4)
    void testGetGroupByNameEndpoint(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<MonitorsGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/name/DETI", HttpMethod.GET,entity,MonitorsGroup.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Monitors from first floor, second and third", response.getBody().getDescription());
    }

    @Test
    @Order(5)
    void testGetGroupByNameEndpointNotFound(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<MonitorsGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/name/DETI10", HttpMethod.GET,entity,MonitorsGroup.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @Order(6)
    void testGetAllMonitorsFromGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1/monitors", HttpMethod.GET,
                entity, new ParameterizedTypeReference<List<Monitor>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @Disabled
    @Order(7)
    void testGetTemplateFromGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1/template", HttpMethod.GET,
                entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("template", response.getBody());
    }

    @Test
    @Order(8)
    void testCreateGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        MonitorsGroup group = new MonitorsGroup();
        group.setName("DAO");
        group.setDescription("Monitors from the entrance");
        group.setMonitors(List.of());
        group.setMadeForMonitor(true);
        HttpEntity<MonitorsGroup> entity = new HttpEntity<>(group, headers);
        ResponseEntity<MonitorsGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.POST,
                entity, MonitorsGroup.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("DAO", response.getBody().getName());
    }

    @Test
    @Order(9)
    void testUpdateGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, new HttpEntity<>(headers), Monitor.class);
        Monitor deti = response.getBody();


        ResponseEntity<MonitorsGroup> response10 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.GET,
                new HttpEntity<>(headers), MonitorsGroup.class);
        MonitorsGroup group = response10.getBody();
        group.setName("ISCA2");
        group.setMonitors(List.of(deti));
        HttpEntity<MonitorsGroup> entity10 = new HttpEntity<>(group, headers);
        ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.PUT,
                entity10, MonitorsGroup.class);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals("ISCA2", response1.getBody().getName());
        assertEquals(1, response1.getBody().getMonitors().size());

    }

    //missing test update monitor in monitorsGroup

    @Test
    @Order(10)
    void testDeleteGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/5", HttpMethod.DELETE,
                entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/5", HttpMethod.GET,
                entity, new ParameterizedTypeReference<MonitorsGroup>() {});
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
    }

}
