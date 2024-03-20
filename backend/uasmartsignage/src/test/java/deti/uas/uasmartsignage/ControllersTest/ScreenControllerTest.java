package deti.uas.uasmartsignage.ControllersTest;

import deti.uas.uasmartsignage.Models.Screen;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.ScreenRepository;
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
    private ScreenRepository screenRepo;

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

        Screen screen = new Screen();
        screen.setLocation(location);
        screen.setStatus(status);

        screen.setMonitorsGroupForScreens(savedMonitorsGroup);

        ResponseEntity<Screen> response = restTemplate.postForEntity("/screens", screen, Screen.class);

        List<Screen> found = screenRepo.findAll();
        System.out.println("huehiehfeio" + found);

        assertThat(found).extracting(Screen::getLocation).containsOnly("Aveiro");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Disabled
    public void whenValid_getAllScreens(){
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        groupRepo.saveAndFlush(monitorsGroup);
        createTestScreen("Aveiro", true, monitorsGroup);
        MonitorsGroup monitorsGroup1 = new MonitorsGroup();
        monitorsGroup1.setName("Group2");
        groupRepo.saveAndFlush(monitorsGroup1);
        createTestScreen("Porto", false, monitorsGroup1);
        ResponseEntity<List<Screen>> response = restTemplate.exchange("/screens", HttpMethod.GET,null,new ParameterizedTypeReference<List<Screen>>() {});
        //System.out.println("111111111111111111111111fueuhfu"+ response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Screen::getLocation).containsExactly("Aveiro", "Porto");
    }

    @Test
    @Disabled
    public void whenValid_getScreenById(){
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        groupRepo.saveAndFlush(monitorsGroup);
        createTestScreen("Aveiro", true, monitorsGroup);
        ResponseEntity<Screen> response = restTemplate.getForEntity("/screens/1", Screen.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getLocation()).isEqualTo("Aveiro");
    }

    private void createTestScreen(String location, Boolean status, MonitorsGroup monitorsGroup) {
        Screen screen = new Screen();
        screen.setLocation(location);
        screen.setStatus(status);
        screen.setMonitorsGroupForScreens(monitorsGroup);
        screenRepo.saveAndFlush(screen);
    }

}
