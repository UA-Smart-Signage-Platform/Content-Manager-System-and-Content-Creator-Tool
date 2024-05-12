package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.CustomFile;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=integration-test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class FileIT {

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

    private static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();

    @BeforeAll
    static void setup(@LocalServerPort int port1) {
        String username = "admin";
        String password = "admin";
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate1.exchange(
                "http://localhost:"+ port1 + "/api/login",
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

    // Important!!!!
    // The tests are ordered because they depend on each other
    // Last one is needed to clean up

    @Test
    @Order(1)
    void testGetFileByIdEndpoint() throws IOException{
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<CustomFile> response = restTemplate.exchange("http://localhost:" +  port  + "/api/files/3", HttpMethod.GET, entity, CustomFile.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(response.getBody()).getId());
        assertEquals("test1.png", response.getBody().getName());
    }

    @Test
    @Order(2)
    void testGetFileByIdEndpointNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files/100",HttpMethod.GET,entity,String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    void testGetRootFilesAndDirectoriesEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files/directory/root", HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    void testDeleteFileByIdEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files/2", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @Order(5)
    void testCreateFileEndpoint() {

        byte[] content = "This is a test file content".getBytes(StandardCharsets.UTF_8);
        // Create a ByteArrayResource from the file content with a custom filename
        ByteArrayResource resource = new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return "test3.png";
            }
        };

        // Set up the request body with the file and parentId
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", resource);
        requestBody.add("parentId", 1);

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(jwtToken);


        // Create the HTTP entity with headers and request body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files", HttpMethod.POST, requestEntity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    // Bad request because file already exists
    @Test
    @Order(6)
    void testCreateFileEndpoint400() {
        byte[] content = "This is a test file content".getBytes(StandardCharsets.UTF_8);
        // Create a ByteArrayResource from the file content with a custom filename
        ByteArrayResource resource = new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return "test3.png";
            }
        };

        // Set up the request body with the file and parentId
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", resource);
        requestBody.add("parentId", 1);

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(jwtToken);

        // Create the HTTP entity with headers and request body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files", HttpMethod.POST, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testCreateDirectoryEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomFile> getResponse = restTemplate.exchange("http://localhost:" + port + "/api/files/1",HttpMethod.GET,entity ,CustomFile.class);
        CustomFile testDir = getResponse.getBody();

        CustomFile directory = new CustomFile();
        directory.setName("New Directory");
        directory.setType("directory");
        directory.setSize(0L);
        directory.setParent(testDir);

        // Set up the request headers

        // Create the HTTP entity with headers and request body
        HttpEntity<CustomFile> requestEntity = new HttpEntity<>(directory, headers);

        ResponseEntity<CustomFile> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/files/directory",
                HttpMethod.POST,
                requestEntity,
                CustomFile.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CustomFile createdDirectory = response.getBody();
        assertEquals("New Directory", createdDirectory.getName());
        assertEquals("directory", createdDirectory.getType());
    }

    @Test
    @Order(8)
    void testCreateDirectoryEndpoint400() {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers1);
        ResponseEntity<CustomFile> getResponse = restTemplate.exchange("http://localhost:" + port + "/api/files/1",HttpMethod.GET,entity ,CustomFile.class);
        CustomFile testDir = getResponse.getBody();

        CustomFile directory = new CustomFile();
        directory.setName("New Directory");
        directory.setType("directory");
        directory.setSize(0L);
        directory.setParent(testDir);

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        // Create the HTTP entity with headers and request body
        HttpEntity<CustomFile> requestEntity = new HttpEntity<>(directory, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/files/directory",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    void testUpdateFileEndpoint() throws IOException  {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers1);
        ResponseEntity<CustomFile> getResponse = restTemplate.exchange("http://localhost:" + port + "/api/files/3", HttpMethod.GET,entity,CustomFile.class);
        CustomFile file = getResponse.getBody();

        file.setName("UpdatedFile.txt");

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        // Create the HTTP entity with headers and request body
        HttpEntity<CustomFile> requestEntity = new HttpEntity<>(file, headers);

        ResponseEntity<CustomFile> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/files/3",
                HttpMethod.PUT,
                requestEntity,
                CustomFile.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomFile updatedFile = response.getBody();
        assertEquals("UpdatedFile.txt", updatedFile.getName());

    }

    @Test
    @Order(10)
    void testUpdateFileEndpoint400() throws IOException  {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers1);
        ResponseEntity<CustomFile> getResponse = restTemplate.exchange("http://localhost:" + port + "/api/files/3", HttpMethod.GET,entity,CustomFile.class);

        System.out.println("response   :"+ getResponse.getBody());
        CustomFile file = getResponse.getBody();

        System.out.println("SADFGHJKJL:"+ file);

        file.setName("UpdatedFile.txt");

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        // Create the HTTP entity with headers and request body
        HttpEntity<CustomFile> requestEntity = new HttpEntity<>(file, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/files/100",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(999)
    // Clean up
    void testDeleteFileByIdWithDirectoryEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files/1", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

}
