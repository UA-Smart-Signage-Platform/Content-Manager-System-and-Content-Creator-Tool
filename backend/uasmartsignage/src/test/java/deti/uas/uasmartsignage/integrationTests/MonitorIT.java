package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

class MonitorIT extends BaseIntegrationTest{

        @LocalServerPort
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        @Order(1)
        void testGetMonitorByIdEndpoint() {
            ResponseEntity<Monitor> response = restTemplate.getForEntity("http://localhost:" + port + "/api/monitors/1", Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getId());
            assertEquals("hall", response.getBody().getName());
        }

        @Test
        @Order(2)
        void testGetMonitorByIdEndpointNotFound() {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/monitors/1000000", String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(3)
        void testGetAllMonitorsEndpoint() {
            ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.GET, null, new ParameterizedTypeReference<List<Monitor>>() {});
            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println(response.getBody());
            assertFalse(response.getBody().isEmpty());
            assertEquals(5, response.getBody().size());
        }

        @Test
        @Order(4)
        void testGetAllMonitorsByPendingEndpoint() {
            ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/pending", HttpMethod.GET, null, new ParameterizedTypeReference<List<Monitor>>() {});
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(2, response.getBody().size());
        }

        @Test
        @Order(5)
        void testDeleteMonitorEndpoint() {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/2", HttpMethod.DELETE, null, String.class);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @Order(6)
        void testAcceptPendingMonitorEndpoint404() {
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/10000/accept", HttpMethod.PUT, null, Monitor.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(7)
        void testAcceptPendingMonitorEndpoint200() {
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/accept/7", HttpMethod.PUT, null, Monitor.class);
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

            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.POST, new HttpEntity<>(group), MonitorsGroup.class);

            assertEquals(HttpStatus.CREATED, response1.getStatusCode());
            assertEquals("update1", response1.getBody().getName());

            Monitor monitor = new Monitor();
            monitor.setUuid("192.168.20");
            monitor.setName("monitor10");
            monitor.setPending(false);
            monitor.setGroup(response1.getBody());
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.POST, new HttpEntity<>(monitor), Monitor.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("monitor10", response.getBody().getName());
            assertEquals("update1", response.getBody().getGroup().getName());
        }

        @Test
        @Order(9)
        //missing change group
        void testUpdateMonitorEndpoint() {
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, null, Monitor.class);
            System.out.println(response.getBody());
            Monitor monitor = response.getBody();
            monitor.setName("update_monitor");

            //not working
            //ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/1", HttpMethod.GET, null , MonitorsGroup.class);

            ResponseEntity<Monitor> response2 = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.PUT, new HttpEntity<>(monitor), Monitor.class);
            assertEquals(HttpStatus.OK, response2.getStatusCode());

            ResponseEntity<Monitor> response3 = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, null, Monitor.class);
            assertEquals(HttpStatus.OK, response3.getStatusCode());

            assertEquals("update_monitor", response3.getBody().getName());
        }

}
