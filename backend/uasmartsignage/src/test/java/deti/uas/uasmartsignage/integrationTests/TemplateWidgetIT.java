package deti.uas.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Models.Widget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateWidgetIT extends BaseIntegrationTest{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public static String jwtToken;
    private static final TestRestTemplate restTemplate1 = new TestRestTemplate();


    @BeforeEach
    void setup() {
        String username = "admin";
        String password = "admin";
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

        assertEquals(200, response.getStatusCodeValue());

        String responseBody = response.getBody();

        JsonElement jsonElement = JsonParser.parseString(responseBody);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        jwtToken = jsonObject.get("jwt").getAsString();
    }

    @Test
    @Order(1)
    void testGetAllTemplateWidgets() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<TemplateWidget>> response = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<TemplateWidget>>() {
        });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody().size());

    }

    @Test
    @Order(2)
    void testGetTemplateWidgetById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TemplateWidget> response = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets/1", HttpMethod.GET, requestEntity, TemplateWidget.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody().getHeight());
        assertEquals(20, response.getBody().getWidth());
    }

    @Test
    @Order(3)
    void testSaveTemplateWidget() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:" + port + "/api/templates/1", HttpMethod.GET, new HttpEntity<>(headers), Template.class);
        Template template1 = response.getBody();

        ResponseEntity<Widget> response2 = restTemplate.exchange("http://localhost:" + port + "/api/widgets/1", HttpMethod.GET, new HttpEntity<>(headers), Widget.class);
        Widget widget1 = response2.getBody();

        TemplateWidget media4 = new TemplateWidget();
        media4.setTop(10);
        media4.setLeftPosition(0);
        media4.setHeight(80);
        media4.setWidth(20);
        media4.setZIndex(1);
        media4.setTemplate(template1);
        media4.setWidget(widget1);

        HttpEntity<TemplateWidget> requestEntity = new HttpEntity<>(media4, headers);

        ResponseEntity<TemplateWidget> response3 = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets", HttpMethod.POST, requestEntity, TemplateWidget.class);
        assertEquals(HttpStatus.CREATED, response3.getStatusCode());
        assertEquals(80, response3.getBody().getHeight());
        assertEquals(20, response3.getBody().getWidth());
        assertEquals(template1.getName(), response3.getBody().getTemplate().getName());
        assertEquals(widget1.getName(), response3.getBody().getWidget().getName());
    }

    @Test
    @Order(4)
    void testUpdateTemplateWidget() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);


        ResponseEntity<Widget> response2 = restTemplate.exchange("http://localhost:" + port + "/api/widgets/1", HttpMethod.GET, new HttpEntity<>(headers), Widget.class);
        Widget widget1 = response2.getBody();

        ResponseEntity<TemplateWidget> response3 = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets/10", HttpMethod.GET, new HttpEntity<>(headers), TemplateWidget.class);

        TemplateWidget media = response3.getBody();

        media.setTop(20);
        media.setWidget(widget1);

        HttpEntity<TemplateWidget> requestEntity = new HttpEntity<>(media, headers);

        ResponseEntity<TemplateWidget> response4 = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets/10", HttpMethod.PUT, requestEntity, TemplateWidget.class);
        assertEquals(HttpStatus.OK, response4.getStatusCode());
        assertEquals(20, response4.getBody().getTop());
        assertEquals(widget1.getName(), response4.getBody().getWidget().getName());
        assertEquals(80,response4.getBody().getHeight());


    }

    @Test
    @Order(5)
    void testDeleteTemplateWidget() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets/10", HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }
}
