package deti.uas.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.After;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseIntegrationTest {

    public static PostgreSQLContainer container;

    static {
        container = (PostgreSQLContainer) new PostgreSQLContainer<>("postgres:latest").withReuse(true)
                .withDatabaseName("uasmartsignageIT")
                .withUsername("integrationTest")
                .withPassword("test");
        container.start();
    }

    public static int getContainerPort() {
        return container.getFirstMappedPort();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeAll
    static void setup() {
        // Prepare the request body with valid credentials
        String username = "admin";
        String password = "admin";
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        int port1 = getContainerPort();
        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make a POST request to your authentication endpoint to get the JWT token
        ResponseEntity<String> response = restTemplate1.exchange(
                "http://localhost:"+ port1 + "/api/login",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //System.out.println("ertyuihgfdsadfgbhnjmkl.,kmjhngbf" + response.getBody());

        // Ensure that the request was successful (HTTP status code 200)
        assertEquals(200, response.getStatusCodeValue());

        // Extract the JWT token from the response body
        String responseBody = response.getBody();

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }
}
