package deti.uas.uasmartsignage.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import deti.uas.uasmartsignage.Models.CustomFile;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:app_it.properties")
public class FileServiceIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetFileByIdEndpoint() {
        ResponseEntity<CustomFile> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/1", CustomFile.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("test", response.getBody().getName());

    }

    @Test
    void testGetFileByIdEndpointNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/100", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetRootFilesAndDirectoriesEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/directory/root", String.class);
        //ResponseEntity<List<CustomFile>> response = restTemplate.exchange("http://localhost:" + port + "/api/files/directory/root", HttpMethod.GET, null, new ParameterizedTypeReference<List<CustomFile>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testGetRootFilesAndDirectoriesEndpointNotFound() {
        //ResponseEntity<List<CustomFile>> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/directory/root1", String.class);
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/files/directory/root1", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
