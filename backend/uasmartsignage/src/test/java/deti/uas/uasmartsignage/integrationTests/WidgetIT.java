package deti.uas.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Services.LogsService;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WidgetIT extends BaseIntegrationTest{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private TemplateGroupService templateGroupService;

    @MockBean
    private LogsService logsService;

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeEach
    void setup() {
        String requestBody = "{\"username\":\"" + "admin" + "\",\"password\":\"" + "admin" + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate1.exchange(
                "http://localhost:"+ port + "/api/login",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String responseBody = response.getBody();

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void testGetAllWidgets() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Widget>> response = restTemplate.exchange("http://localhost:" + port + "/api/widgets", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Widget>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(6, response.getBody().size());
    }

    @Test
    @Order(2)
    void testGetWidgetById(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Widget> response = restTemplate.exchange("http://localhost:" + port + "/api/widgets/1", HttpMethod.GET, requestEntity, Widget.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Weather", response.getBody().getName());
        assertEquals("static/widgets/weather.widget", response.getBody().getPath());
    }

    @Test
    @Order(3)
    void testSaveWidget(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Widget weatherWidget = new Widget();
        weatherWidget.setName("Sports");
        weatherWidget.setPath("static/widgets/sports.widget");
        weatherWidget.setContents(new ArrayList<>());

        HttpEntity<Widget> requestEntity = new HttpEntity<>(weatherWidget, headers);

        ResponseEntity<Widget> response = restTemplate.exchange("http://localhost:" + port + "/api/widgets", HttpMethod.POST, requestEntity, Widget.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Sports", response.getBody().getName());
        assertEquals("static/widgets/sports.widget", response.getBody().getPath());
    }

    @Test
    @Order(4)
    void testUpdateWidget(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        Content content = new Content();
        content.setName("Update content");
        content.setType("update test");
        content.setOptions(List.of("test1", "test2", "test3"));

        HttpEntity<Content> requestEntity = new HttpEntity<>(content, headers);

        ResponseEntity<Content> response = restTemplate.exchange("http://localhost:" + port + "/content", HttpMethod.POST, requestEntity, Content.class);
        Content content1 = response.getBody();

        ResponseEntity<Widget> response1 = restTemplate.exchange("http://localhost:" + port + "/api/widgets/1", HttpMethod.GET, new HttpEntity<>(headers), Widget.class);
        Widget widget = response1.getBody();

        widget.setName("Updated widget");
        widget.setContents(List.of(content1));

        HttpEntity<Widget> requestEntity2 = new HttpEntity<>(widget, headers);

        ResponseEntity<Widget> response2 = restTemplate.exchange("http://localhost:" + port + "/api/widgets/1", HttpMethod.PUT, requestEntity2, Widget.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals("Updated widget", response2.getBody().getName());
        assertEquals(1,response2.getBody().getContents().size());
        assertEquals("Update content", response2.getBody().getContents().get(0).getName());
    }

    @Test
    @Order(5)
    void testDeleteWidget(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange("http://localhost:" + port + "/api/widgets/6", HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
