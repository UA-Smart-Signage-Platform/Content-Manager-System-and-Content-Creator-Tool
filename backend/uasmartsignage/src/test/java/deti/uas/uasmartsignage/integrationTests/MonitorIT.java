package deti.uas.uasmartsignage.integrationTests;

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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;

class MonitorIT extends BaseIntegrationTest{

        @LocalServerPort
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;




        @Test
        @Order(1)
        void testGetMonitorByIdEndpoint() {
            HttpHeaders headers = new HttpHeaders();
            System.out.println("wfdgdc"+jwtToken);
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
            System.out.println("wfdgdc"+jwtToken);
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
            System.out.println(response.getBody());
            assertFalse(response.getBody().isEmpty());
            assertEquals(5, response.getBody().size());
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
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(7)
        void testAcceptPendingMonitorEndpoint200() {
            HttpHeaders headers = new HttpHeaders();
            System.out.println("wfdgdc"+jwtToken);
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

            MonitorsGroup group = new MonitorsGroup();
            group.setName("update1");
            group.setMonitors(List.of());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<MonitorsGroup> entity = new HttpEntity<>(group, headers);

            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.POST, entity, MonitorsGroup.class);

            assertEquals(HttpStatus.CREATED, response1.getStatusCode());
            assertEquals("update1", response1.getBody().getName());

            Monitor monitor = new Monitor();
            monitor.setUuid("192.168.20");
            monitor.setName("monitor10");
            monitor.setPending(false);
            monitor.setGroup(response1.getBody());

            HttpEntity<Monitor> entity2 = new HttpEntity<>(monitor, headers);

            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.POST, entity2, Monitor.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("monitor10", response.getBody().getName());
            assertEquals("update1", response.getBody().getGroup().getName());
        }

        @Test
        @Order(9)
        //missing change group
        void testUpdateMonitorEndpoint() {
            MonitorsGroup group = new MonitorsGroup();
            group.setName("updated_group");
            group.setMonitors(List.of());
            group.setId(4L);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);


            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, entity, Monitor.class);
            Monitor monitor = response.getBody();
            monitor.setName("update_monitor");

            HttpEntity<MonitorsGroup> entity2 = new HttpEntity<>(group,headers);

            ResponseEntity<MonitorsGroup> response_post = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.POST, entity2, MonitorsGroup.class);
            assertEquals(HttpStatus.CREATED, response_post.getStatusCode());

            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.GET, entity , MonitorsGroup.class);
            assertEquals(HttpStatus.OK, response1.getStatusCode());
            monitor.setGroup(response1.getBody());

            HttpEntity<Monitor> entity3 = new HttpEntity<>(monitor, headers);

            ResponseEntity<Monitor> response2 = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.PUT, entity3, Monitor.class);
            assertEquals(HttpStatus.OK, response2.getStatusCode());

            ResponseEntity<Monitor> response3 = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, entity, Monitor.class);
            assertEquals(HttpStatus.OK, response3.getStatusCode());

            assertEquals("update_monitor", response3.getBody().getName());
            assertEquals("updated_group", response3.getBody().getGroup().getName());
        }

}
