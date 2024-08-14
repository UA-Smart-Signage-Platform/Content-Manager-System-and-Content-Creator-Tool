// package pt.ua.deti.uasmartsignage.integrationTests;

// import static org.junit.jupiter.api.Assertions.assertEquals;


// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;
// import pt.ua.deti.uasmartsignage.models.Monitor;
// import pt.ua.deti.uasmartsignage.models.MonitorGroup;
// import pt.ua.deti.uasmartsignage.models.Rule;
// import pt.ua.deti.uasmartsignage.services.LogsService;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.*;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;


// import java.util.List;

// @Testcontainers
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// class MonitorGroupIT {

//     @MockBean
//     private LogsService logsService;

//     @Container
//     public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
//             .withDatabaseName("uasmartsignageIT")
//             .withUsername("integrationTest")
//             .withPassword("test");

//     @DynamicPropertySource
//     static void properties(DynamicPropertyRegistry registry) {
//         registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//         registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//         registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//     }

//     @LocalServerPort
//     private int port;

//     @Autowired
//     private TestRestTemplate restTemplate;

//     public static String jwtToken;
//     private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


//     @BeforeEach
//     void setup() {
//         String requestBody = "{\"username\":\"" + "admin" + "\",\"password\":\"" + "admin" + "\"}";

//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Content-Type", "application/json");

//         HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

//         ResponseEntity<String> response = restTemplate1.exchange(
//                 "http://localhost:"+ port + "/api/login",
//                 HttpMethod.POST,
//                 requestEntity,
//                 String.class
//         );

//         assertEquals(HttpStatus.OK, response.getStatusCode());

//         String responseBody = response.getBody();

//         JsonElement jsonElement = JsonParser.parseString(responseBody);
//         JsonObject jsonObject = jsonElement.getAsJsonObject();

//         jwtToken = jsonObject.get("jwt").getAsString();
//     }



//     @Test
//     @Order(1)
//     void testGetAllMonitorGroups() {
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<List<MonitorGroup>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.GET,
//                 entity, new ParameterizedTypeReference<List<MonitorGroup>>() {});
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(4, response.getBody().size());
//     }

//     @Test
//     @Order(2)
//     void testGetMonitorGroupsNotMadeForMonitors(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<List<MonitorGroup>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/notMadeForMonitor", HttpMethod.GET,
//                 entity, new ParameterizedTypeReference<List<MonitorGroup>>() {});
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(3, response.getBody().size());
//     }

//     @Test
//     @Order(3)
//     void testGetGroupByIdEndpoint(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<MonitorGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1", HttpMethod.GET,entity,MonitorGroup.class);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(1, response.getBody().getId());
//         assertEquals("DETI", response.getBody().getName());
//     }

//     @Test
//     @Order(4)
//     void testGetGroupByNameEndpoint(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<MonitorGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/name/DETI", HttpMethod.GET,entity,MonitorGroup.class);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(1, response.getBody().getId());
//         assertEquals("Monitors from first floor, second and third", response.getBody().getDescription());
//     }

//     @Test
//     @Order(5)
//     void testGetGroupByNameEndpointNotFound(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<MonitorGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/name/DETI10", HttpMethod.GET,entity,MonitorGroup.class);
//         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     }

//     @Test
//     @Order(6)
//     void testGetAllMonitorsFromGroup(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<List<Monitor>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1/monitors", HttpMethod.GET,
//                 entity, new ParameterizedTypeReference<List<Monitor>>() {});
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(2, response.getBody().size());
//     }

//     @Test
//     @Order(7)
//     void testCreateGroup(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         MonitorGroup group = new MonitorGroup();
//         group.setName("DAO");
//         group.setDescription("Monitors from the entrance");
//         group.setMonitors(List.of());
//         group.setDefaultGroup(true);
//         HttpEntity<MonitorGroup> entity = new HttpEntity<>(group, headers);
//         ResponseEntity<MonitorGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.POST,
//                 entity, MonitorGroup.class);
//         assertEquals(HttpStatus.CREATED, response.getStatusCode());
//         assertEquals("DAO", response.getBody().getName());
//     }

//     @Test
//     @Order(8)
//     void testUpdateGroup(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         headers.setContentType(MediaType.APPLICATION_JSON);

//         ResponseEntity<Monitor> response = restTemplate.exchange("http://localhost:" + port + "/api/monitors/1", HttpMethod.GET, new HttpEntity<>(headers), Monitor.class);
//         Monitor deca = response.getBody();

//         ResponseEntity<MonitorGroup> response10 = restTemplate.exchange("http://localhost:" + port + "/api/groups/3", HttpMethod.GET,
//                 new HttpEntity<>(headers), MonitorGroup.class);
//         MonitorGroup group = response10.getBody();

//         group.setName("DECA2");
//         group.setMonitors(List.of(deca));

//         HttpEntity<MonitorGroup> entity10 = new HttpEntity<>(group, headers);
//         ResponseEntity<MonitorGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/4", HttpMethod.PUT,
//                 entity10, MonitorGroup.class);
//         assertEquals(HttpStatus.OK, response1.getStatusCode());
//         assertEquals("DECA2", response1.getBody().getName());
//         assertEquals(1, response1.getBody().getMonitors().size());
//         assertEquals("car2222", response1.getBody().getMonitors().get(0).getName());

//     }

//     @Test
//     @Order(9)
//     void testDeleteGroup(){
//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(jwtToken);
//         HttpEntity<?> entity = new HttpEntity<>(headers);
//         ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/5", HttpMethod.DELETE,
//                 entity, String.class);
//         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

//         ResponseEntity<MonitorGroup> response1 = restTemplate.exchange("http://localhost:" + port + "/api/groups/5", HttpMethod.GET,
//                 entity, new ParameterizedTypeReference<MonitorGroup>() {});
//         assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
//     }

// }
