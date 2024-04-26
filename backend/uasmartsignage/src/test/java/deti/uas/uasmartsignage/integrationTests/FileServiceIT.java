package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import deti.uas.uasmartsignage.Models.CustomFile;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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

class FileServiceIT extends BaseIntegrationTest{


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Important!!!!
    // The tests are ordered because they depend on each other
    // Last one is needed to clean up

    @Test
    @Order(1)
    void testGetFileByIdEndpoint() throws IOException{
        ResponseEntity<CustomFile> response = restTemplate.getForEntity("http://localhost:" +  port  + "/api/files/3", CustomFile.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(response.getBody()).getId());
        assertEquals("test1.png", response.getBody().getName());
    }

    @Test
    @Order(2)
    void testGetFileByIdEndpointNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/100", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    void testGetRootFilesAndDirectoriesEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/directory/root", String.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    void testDeleteFileByIdEndpoint() {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files/2", HttpMethod.DELETE, null, String.class);
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

        // Create the HTTP entity with headers and request body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files", HttpMethod.POST, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testCreateDirectoryEndpoint() {
        ResponseEntity<CustomFile> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/files/1", CustomFile.class);
        CustomFile testDir = getResponse.getBody();

        CustomFile directory = new CustomFile();
        directory.setName("New Directory");
        directory.setType("directory");
        directory.setSize(0L);
        directory.setParent(testDir);

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
        ResponseEntity<CustomFile> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/files/1", CustomFile.class);
        CustomFile testDir = getResponse.getBody();

        CustomFile directory = new CustomFile();
        directory.setName("New Directory");
        directory.setType("directory");
        directory.setSize(0L);
        directory.setParent(testDir);

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
        ResponseEntity<CustomFile> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/files/3", CustomFile.class);
        CustomFile file = getResponse.getBody();

        file.setName("UpdatedFile.txt");

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
        ResponseEntity<CustomFile> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/files/3", CustomFile.class);
        CustomFile file = getResponse.getBody();

        file.setName("UpdatedFile.txt");

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/files/1", HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

}
