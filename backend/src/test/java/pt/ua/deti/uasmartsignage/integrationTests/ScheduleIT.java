package pt.ua.deti.uasmartsignage.integrationTests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pt.ua.deti.uasmartsignage.models.Schedule;
import pt.ua.deti.uasmartsignage.services.LogsService;
import pt.ua.deti.uasmartsignage.services.TemplateGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleIT extends BaseIntegrationTest{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private LogsService logsService;

    @MockBean
    private TemplateGroupService templateGroupService;

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
    void testGetAllSchedules() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Schedule>> response = restTemplate.exchange("http://localhost:"+ port + "/api/schedules", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Schedule>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @Order(2)
    void testGetScheduleById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Schedule> response = restTemplate.exchange("http://localhost:"+ port + "/api/schedules/1", HttpMethod.GET, requestEntity, Schedule.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LocalDate.parse("2024-04-21"), response.getBody().getStartDate());
        assertEquals(LocalTime.parse("00:00"), response.getBody().getStartTime());
    }

    @Test
    @Order(3)
    void testSaveSchedule(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Schedule schedule = new Schedule();
        List<Integer> days = new ArrayList<>();
        days.add(1);
        days.add(2);
        days.add(3);
        days.add(4);
        days.add(5);
        schedule.setWeekdays(days);
        schedule.setEndDate(LocalDate.parse("2024-05-11"));
        schedule.setStartDate(LocalDate.parse("2024-05-11"));
        schedule.setStartTime(LocalTime.parse("08:00"));
        schedule.setEndTime(LocalTime.parse("22:30"));

        HttpEntity<Schedule> requestEntity1 = new HttpEntity<>(schedule, headers);

        ResponseEntity<Schedule> response1 = restTemplate.exchange("http://localhost:"+ port + "/api/schedules", HttpMethod.POST, requestEntity1, Schedule.class);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        assertEquals(LocalDate.parse("2024-05-11"), response1.getBody().getStartDate());
        assertEquals(LocalTime.parse("08:00"), response1.getBody().getStartTime());
    }

    @Test
    @Order(4)
    void tesUpdateSchedule(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        ResponseEntity<Schedule> response = restTemplate.exchange("http://localhost:"+ port + "/api/schedules/1", HttpMethod.GET, new HttpEntity<>(headers), Schedule.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Schedule schedule = response.getBody();
        schedule.setStartDate(LocalDate.parse("2024-05-11"));
        schedule.setStartTime(LocalTime.parse("08:30"));

        HttpEntity<Schedule> requestEntity = new HttpEntity<>(schedule, headers);

        ResponseEntity<Schedule> response1 = restTemplate.exchange("http://localhost:"+ port + "/api/schedules/1", HttpMethod.PUT, requestEntity, Schedule.class);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(LocalDate.parse("2024-05-11"), response1.getBody().getStartDate());
        assertEquals(LocalTime.parse("08:30"), response1.getBody().getStartTime());
    }

    @Test
    @Order(5)
    void testUpdateMultipleSchedules(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        ResponseEntity<Schedule> response = restTemplate.exchange("http://localhost:"+ port + "/api/schedules/1", HttpMethod.GET, new HttpEntity<>(headers), Schedule.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Schedule schedule = response.getBody();
        schedule.setPriority(2);

        ResponseEntity<Schedule> response2 = restTemplate.exchange("http://localhost:"+ port + "/api/schedules/2", HttpMethod.GET, new HttpEntity<>(headers), Schedule.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        Schedule schedule2 = response2.getBody();
        schedule2.setPriority(3);


        HttpEntity<List<Schedule>> requestEntity = new HttpEntity<>(List.of(schedule,schedule2), headers);

        ResponseEntity<List<Schedule>> response1 = restTemplate.exchange("http://localhost:"+ port + "/api/schedules", HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(2, response1.getBody().size());
        assertEquals(2, response1.getBody().get(0).getPriority());
        assertEquals(3, response1.getBody().get(1).getPriority());

    }

}
