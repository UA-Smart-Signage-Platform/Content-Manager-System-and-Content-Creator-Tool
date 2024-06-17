package pt.ua.deti.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pt.ua.deti.uasmartsignage.Models.Monitor;
import pt.ua.deti.uasmartsignage.Models.MonitorsGroup;
import pt.ua.deti.uasmartsignage.Services.LogsService;
import pt.ua.deti.uasmartsignage.Services.TemplateGroupService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.List;

class MonitorIT extends BaseIntegrationTest{

    @LocalServerPort
    private int port;

    @MockBean
    private LogsService logsService;

    @MockBean
    private TemplateGroupService templateGroupService;

    @Autowired
    private TestRestTemplate restTemplate;

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeEach
    void setup() {
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

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String responseBody = response.getBody();

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }





    @Test
        @Order(1)
        void testGetMonitorByIdEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Monitor> response = restTemplate.exchange(
                    "http://localhost:" + port + "/api/monitors/1",
                    HttpMethod.GET,
                    entity,
                    Monitor.class
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getId());
            assertEquals("hall", response.getBody().getName());
        }

        @Test
        @Order(2)
        void testGetMonitorByIdEndpointNotFound() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1000000",HttpMethod.GET, entity, String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(3)
        void testGetAllMonitorsEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.GET,
                    entity, new ParameterizedTypeReference<List<Monitor>>() {});
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(6, response.getBody().size());
        }

        @Test
        @Order(4)
        void testGetAllMonitorsByPendingEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/pending", HttpMethod.GET,
                    entity, new ParameterizedTypeReference<List<Monitor>>() {});
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(2, response.getBody().size());
        }

        @Test
        @Order(5)
        void testDeleteMonitorEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/2", HttpMethod.DELETE, entity, String.class);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @Order(6)
        void testAcceptPendingMonitorEndpoint404() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/10000/accept", HttpMethod.PUT, entity, Monitor.class);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        @Order(7)
        void testAcceptPendingMonitorEndpoint200() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/accept/7", HttpMethod.PUT, entity, Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isPending());
            assertEquals("car2", response.getBody().getName());
        }

        @Test
        @Order(8)
        void testCreateMonitorEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<?> entity = new HttpEntity<>( headers);

            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.GET, entity , MonitorsGroup.class);

            Monitor monitor = new Monitor();
            monitor.setUuid("192.168.20");
            monitor.setName("monitor10");
            monitor.setPending(false);
            monitor.setGroup(response1.getBody());

            HttpEntity<Monitor> entity2 = new HttpEntity<>(monitor, headers);

            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.POST, entity2, Monitor.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("monitor10", response.getBody().getName());
            assertEquals("DECA", response.getBody().getGroup().getName());
        }

        @Test
        @Order(9)
        void testUpdateMonitorEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);


            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, entity, Monitor.class);
            Monitor monitor = response.getBody();
            monitor.setName("update_monitor");

            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.GET, entity , MonitorsGroup.class);
            assertEquals(HttpStatus.OK, response1.getStatusCode());
            monitor.setGroup(response1.getBody());

            HttpEntity<Monitor> entity3 = new HttpEntity<>(monitor, headers);

            ResponseEntity<Monitor> response2 = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.PUT, entity3, Monitor.class);
            assertEquals(HttpStatus.OK, response2.getStatusCode());

            ResponseEntity<Monitor> response3 = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, entity, Monitor.class);
            assertEquals(HttpStatus.OK, response3.getStatusCode());

            assertEquals("update_monitor", response3.getBody().getName());
            assertEquals("DECA", response3.getBody().getGroup().getName());
        }

}
