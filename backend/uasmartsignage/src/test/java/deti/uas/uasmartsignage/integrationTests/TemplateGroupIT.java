package deti.uas.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.*;
import deti.uas.uasmartsignage.Services.LogsService;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TemplateGroupIT{

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("uasmartsignageIT")
            .withUsername("integrationTest")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }

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
                "http://localhost:" + port + "/api/login",
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
    void testGetAllTemplateGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<TemplateGroup>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/templateGroups", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<TemplateGroup>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    @Order(2)
    void testGetTemplateGroupById(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TemplateGroup> response = restTemplate.exchange("http://localhost:"+ port + "/api/templateGroups/1", HttpMethod.GET, requestEntity, TemplateGroup.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals("DETI", response.getBody().getGroup().getName());
       assertEquals("template1",response.getBody().getTemplate().getName());
       assertEquals(LocalDate.parse("2024-04-21"),response.getBody().getSchedule().getEndDate());
    }

    @Test
    @Order(3)
    void testSaveTemplateGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/1", HttpMethod.GET, new HttpEntity<>(headers), Template.class);
        Template template1 = response.getBody();

        ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:"+ port + "/api/groups/3", HttpMethod.GET, new HttpEntity<>(headers), MonitorsGroup.class);
        MonitorsGroup dbio = response1.getBody();

        ResponseEntity<Schedule> response3 = restTemplate.exchange("http://localhost:"+ port + "/api/schedules/1", HttpMethod.GET, new HttpEntity<>(headers), Schedule.class);
        Schedule schedule = response3.getBody();

        TemplateGroup templateGroup1 = new TemplateGroup();
        templateGroup1.setGroup(dbio);
        templateGroup1.setTemplate(template1);
        templateGroup1.setSchedule(schedule);
        templateGroup1.setContent(Map.of(1, "content"));

        HttpEntity<TemplateGroup> requestEntity = new HttpEntity<>(templateGroup1, headers);

        ResponseEntity<TemplateGroup> response2 = restTemplate.exchange("http://localhost:"+ port + "/api/templateGroups", HttpMethod.POST, requestEntity, TemplateGroup.class);
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        assertEquals("DBIO", response2.getBody().getGroup().getName());
        assertEquals("template1", response2.getBody().getTemplate().getName());
    }

    @Test
    @Order(4)
    void testUpdateTemplateGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TemplateGroup> response = restTemplate.exchange("http://localhost:"+ port + "/api/templateGroups/1", HttpMethod.GET, requestEntity, TemplateGroup.class);
        TemplateGroup templateGroup = response.getBody();

        /*ResponseEntity<Template> response1 = restTemplate.exchange("http://localhost:"+ port + "/api/templates/2", HttpMethod.GET, new HttpEntity<>(headers), Template.class);
        Template template = response1.getBody();

        templateGroup.setTemplate(template);*/

        ResponseEntity<MonitorsGroup> response1 = restTemplate.exchange("http://localhost:"+ port + "/api/groups/2", HttpMethod.GET, new HttpEntity<>(headers), MonitorsGroup.class);
        MonitorsGroup monitorsGroup = response1.getBody();

        templateGroup.setGroup(monitorsGroup);

        HttpEntity<TemplateGroup> requestEntity2 = new HttpEntity<>(templateGroup, headers);
        ResponseEntity<TemplateGroup> response2 = restTemplate.exchange("http://localhost:"+ port + "/api/templateGroups/1", HttpMethod.PUT, requestEntity2, TemplateGroup.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals("DMAT", response2.getBody().getGroup().getName());
        //assertEquals("template2", response2.getBody().getTemplate().getName());
    }

    @Test
    @Order(5)
    void testDeleteTemplateGroup(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:"+ port + "/api/templateGroups/3", HttpMethod.DELETE, requestEntity, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());}




}
