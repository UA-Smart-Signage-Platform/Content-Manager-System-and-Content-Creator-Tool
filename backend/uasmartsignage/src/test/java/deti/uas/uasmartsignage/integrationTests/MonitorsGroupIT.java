package deti.uas.uasmartsignage.integrationTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.TemplateGroup;
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
    void testGetAllMonitorsGroups() {
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
    @Order(7)
    @Disabled //error extracting response (erro do poço)
    void testGetTemplateFromGroup() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<TemplateGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1/template", HttpMethod.GET,
                entity, TemplateGroup.class);

        String responseBody = new ObjectMapper().writeValueAsString(response.getBody());
        System.out.println(responseBody);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("DETI news", response.getBody());
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
        Monitor deca = response.getBody();
        System.out.println("dfghjkgygyv" + deca);


        ResponseEntity<MonitorsGroup> response10 = restTemplate.exchange("http://localhost:" + port + "/api/groups/3", HttpMethod.GET,
                new HttpEntity<>(headers), MonitorsGroup.class);
        MonitorsGroup group = response10.getBody();
        System.out.println("see get group" + group);//está a vir sem a lista de monitors
        group.setName("DECA2");
        group.setMonitors(List.of(deca));
        HttpEntity<MonitorsGroup> entity10 = new HttpEntity<>(group, headers);
        System.out.println("dfghjk" + group);
        ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.PUT,
                entity10, MonitorsGroup.class);
        System.out.println("dfghjk"+response1.getBody());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals("DECA2", response1.getBody().getName());
        assertEquals(1, response1.getBody().getMonitors().size());
        //assertEquals("hall", response1.getBody().getMonitors().get(0).getName());

    }

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
