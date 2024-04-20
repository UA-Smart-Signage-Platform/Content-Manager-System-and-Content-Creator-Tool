package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.*;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@Testcontainers
public class MonitorIT{
        @Container
        public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("uasmartsignageIT")
                .withUsername("integrationTest")
                .withPassword("test");


        @DynamicPropertySource
        static void properties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        }

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
            assertEquals(2, response.getBody().size());
        }

        @Test
        void testDeleteMonitorEndpoint() {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/2", HttpMethod.DELETE, null, String.class);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
        }

        @Test
        void testAcceptPendingMonitorEndpoint404() {
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1/accept", HttpMethod.PUT, null, Monitor.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void testAcceptPendingMonitorEndpoint200() {
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/accept/7", HttpMethod.PUT, null, Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().isPending());
            assertEquals("car2", response.getBody().getName());
        }

        @Test
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
            monitor.setPending(false);
            monitor.setGroup(response1.getBody());
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors", HttpMethod.POST, new HttpEntity<>(monitor), Monitor.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("monitor10", response.getBody().getName());

        }

        @Test
        void testUpdateMonitorEndpoint() {
            Monitor monitor = new Monitor();
            monitor.setName("update_monitor");
            monitor.setPending(false);
            ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/1", HttpMethod.GET, null, MonitorsGroup.class);
            monitor.setGroup(response1.getBody());
            ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/6", HttpMethod.PUT, new HttpEntity<>(monitor), Monitor.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("update_monitor", response.getBody().getName());
            assertEquals("deti", response.getBody().getGroup().getName());
        }



}
