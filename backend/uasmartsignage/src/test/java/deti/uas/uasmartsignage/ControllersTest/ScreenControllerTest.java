package deti.uas.uasmartsignage.ControllersTest;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.junit.jupiter.api.Disabled;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ScreenControllerTest {

    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MonitorRepository screenRepo;

    @Autowired
    private MonitorGroupRepository groupRepo;

    @AfterEach
    public void resetDb() {
        screenRepo.deleteAll();
        groupRepo.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateScreen() {
        String location = "Aveiro";
        Boolean status = true;
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        ResponseEntity<MonitorsGroup> response1 = restTemplate.postForEntity("/groups", monitorsGroup, MonitorsGroup.class);

        MonitorsGroup savedMonitorsGroup = response1.getBody();

        Monitor screen = new Monitor();
        screen.setLocation(location);
        screen.setStatus(status);

        screen.setMonitorsGroupForScreens(savedMonitorsGroup);

        ResponseEntity<Monitor> response = restTemplate.postForEntity("/monitors", screen, Monitor.class);

        System.out.println("RESPONSE RECEIVED");

        List<Monitor> found = screenRepo.findAll();
        System.out.println("huehiehfeio" + found);

        assertThat(found).extracting(Monitor::getLocation).containsOnly("Aveiro");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void whenValid_getAllScreens(){
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        groupRepo.saveAndFlush(monitorsGroup);
        createTestScreen("Aveiro", true, monitorsGroup);
        MonitorsGroup monitorsGroup1 = new MonitorsGroup();
        monitorsGroup1.setName("Group2");
        groupRepo.saveAndFlush(monitorsGroup1);
        createTestScreen("Porto", false, monitorsGroup1);
        ResponseEntity<List<Monitor>> response = restTemplate.exchange("/monitors", HttpMethod.GET,null,new ParameterizedTypeReference<List<Monitor>>() {});
        //System.out.println("111111111111111111111111fueuhfu"+ response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Monitor::getLocation).containsExactly("Aveiro", "Porto");
    }

    @Test
    public void whenValid_getScreenById(){
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        groupRepo.saveAndFlush(monitorsGroup);
        createTestScreen("Aveiro", true, monitorsGroup);
        ResponseEntity<Monitor> response = restTemplate.getForEntity("/monitors/1", Monitor.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getLocation()).isEqualTo("Aveiro");
    }

    private void createTestScreen(String location, Boolean status, MonitorsGroup monitorsGroup) {
        Monitor screen = new Monitor();
        screen.setLocation(location);
        screen.setStatus(status);
        screen.setMonitorsGroupForScreens(monitorsGroup);
        screenRepo.saveAndFlush(screen);
    }

}
