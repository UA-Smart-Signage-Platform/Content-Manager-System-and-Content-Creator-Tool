package deti.uas.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import deti.uas.uasmartsignage.Models.Template;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateIT  extends BaseIntegrationTest{
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
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

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
    void testGetAllTemplates(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Template>> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Template>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());
    }

    @Test
    @Order(2)
    void testGetTemplateById(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/1", HttpMethod.GET, requestEntity, Template.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("template1", response.getBody().getName());

    }

    @Test
    @Order(3)//dont know how to do(templateWidget needs template to save adn template needs TemplateWidget to save)
    void testSaveTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Fetch an existing Widget
        ResponseEntity<Widget> widgetResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/widgets/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Widget.class);
        Widget widget = widgetResponse.getBody();

        System.out.println("sdfdg"+widget);

        /* // Prepare a TemplateWidget
        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setTop(0);
        templateWidget.setLeftPosition(0);
        templateWidget.setWidth(100);
        templateWidget.setHeight(100);
        templateWidget.setZIndex(1);
        templateWidget.setWidget(widget);

        ResponseEntity<TemplateWidget> response1 = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets", HttpMethod.POST, new HttpEntity<>(templateWidget, headers), TemplateWidget.class);

        TemplateWidget tw = response1.getBody();*/

        ResponseEntity<TemplateWidget> response1 = restTemplate.exchange("http://localhost:" + port + "/api/templateWidgets/1", HttpMethod.GET, new HttpEntity<>(headers), TemplateWidget.class);
        TemplateWidget tw = response1.getBody();

        // We don't save the TemplateWidget separately; instead, we link it to the Template and save the Template
        Template template = new Template();
        template.setName("template5");
        template.setTemplateWidgets(List.of(tw));

        HttpEntity<Template> requestEntity = new HttpEntity<>(template, headers);

        // Save the Template (which includes the TemplateWidget)
        ResponseEntity<Template> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/templates",
                HttpMethod.POST,
                requestEntity,
                Template.class);

        // Assertions to verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("template5", response.getBody().getName());
        assertEquals(1, response.getBody().getTemplateWidgets().size());
        //assertEquals(templateWidget.getTop(), response.getBody().getTemplateWidgets().get(0).getTop());
        //assertEquals(templateWidget.getLeftPosition(), response.getBody().getTemplateWidgets().get(0).getLeftPosition());
        //assertEquals(templateWidget.getWidth(), response.getBody().getTemplateWidgets().get(0).getWidth());
        //assertEquals(templateWidget.getHeight(), response.getBody().getTemplateWidgets().get(0).getHeight());
        //assertEquals(templateWidget.getZIndex(), response.getBody().getTemplateWidgets().get(0).getZIndex());
    }

    @Test
    @Order(4)
    void testUpdateTemplate(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Template> requestEntity = new HttpEntity<>( headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/3", HttpMethod.GET, requestEntity, Template.class);
        Template template = response.getBody();

        template.setName("updatedTemplate");

        HttpEntity<Template> updateentity = new HttpEntity<>(template, headers);

        ResponseEntity<Template> response2 = restTemplate.exchange("http://localhost:"+ port + "/api/templates/3", HttpMethod.PUT, updateentity, Template.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals("updatedTemplate", response2.getBody().getName());



    }

    @Test
    @Order(5)
    void testDeleteTemplate(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Template> response = restTemplate.exchange("http://localhost:"+ port + "/api/templates/4", HttpMethod.DELETE, requestEntity, Template.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); }
}
