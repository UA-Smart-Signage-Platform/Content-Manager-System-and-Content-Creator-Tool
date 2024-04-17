package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:app_it.properties")
public class MonitorIT {

        @LocalServerPort
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        void testGetMonitorByIdEndpoint() {
            ResponseEntity<Monitor> response = restTemplate.getForEntity("http://localhost:" + port + "/api/monitors/1", Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().getId());
            assertEquals("hall", response.getBody().getName());
        }

        @Test
        void testGetMonitorByIdEndpointNotFound() {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/monitors/1000000", String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void testGetAllMonitorsEndpoint() {
            ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.GET, null, new ParameterizedTypeReference<List<Monitor>>() {});
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(5, response.getBody().size());
        }

        @Test
        void testGetAllMonitorsByPendingEndpoint() {
            ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/pending", HttpMethod.GET, null, new ParameterizedTypeReference<List<Monitor>>() {});
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isEmpty());
            assertEquals(3, response.getBody().size());
        }

        @Test
        @Disabled
        void testDeleteMonitorEndpoint() {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.DELETE, null, String.class);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        @Disabled
        void testAcceptPendingMonitorEndpoint() {
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1/accept", HttpMethod.PUT, null, Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isPending());
            assertEquals("hall", response.getBody().getName());
        }

        @Test
        @Disabled  //update or create-drop on app_it.properties // other possibility is to use a different database
        void testCreateMonitorEndpoint() {
            MonitorsGroup group = new MonitorsGroup();
            group.setName("update1");
            group.setMonitors(List.of());

            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.POST, new HttpEntity<>(group), MonitorsGroup.class);
            assertEquals(HttpStatus.CREATED, response1.getStatusCode());
            assertEquals("update1", response1.getBody().getName());

            Monitor monitor = new Monitor();
            monitor.setIp("192.168.20");
            monitor.setName("monitor10");
            monitor.setPending(true);
            monitor.setGroup(response1.getBody());
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.POST, new HttpEntity<>(monitor), Monitor.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("monitor10", response.getBody().getName());

        }

        @Test
        @Disabled
        void testUpdateMonitorEndpoint() {
            Monitor monitor = new Monitor();
            monitor.setIp("192.168.7");
            monitor.setName("car2");
            monitor.setPending(true);
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.PUT, new HttpEntity<>(monitor), Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("car2", response.getBody().getName());
        }



}
