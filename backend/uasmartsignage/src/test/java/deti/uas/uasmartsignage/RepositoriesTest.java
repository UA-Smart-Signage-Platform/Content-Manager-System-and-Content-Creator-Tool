package deti.uas.uasmartsignage;

import deti.uas.uasmartsignage.Models.Screen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import deti.uas.uasmartsignage.Repositories.ScreenRepository;
import deti.uas.uasmartsignage.Models.MonitorsGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoriesTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private ScreenRepository screenRepository;

        @Test
        @DisplayName("When findById then return Screen")
        public void whenFindById_thenReturnScreen() {
            String location = "Aveiro";
            Screen screen = new Screen();
            screen.setLocation(location);
            screen.setStatus(true);
            MonitorsGroup monitorsGroup = new MonitorsGroup();
            monitorsGroup.setName("Group1");
            entityManager.persistAndFlush(monitorsGroup);
            screen.setMonitorsGroupForScreens(monitorsGroup);
            entityManager.persistAndFlush(screen);

            Screen found = screenRepository.findById(screen.getId()).orElse(null);

            assertThat(found.getLocation())
                    .isEqualTo("Aveiro");
        }

        @Test
        @DisplayName("When findByMonitorsGroupForScreens then return Screens")
        public void whenFindByMonitorsGroupForScreens_thenReturnScreens() {
            String location = "Aveiro";
            Screen screen = new Screen();
            screen.setLocation(location);
            screen.setStatus(true);
            MonitorsGroup monitorsGroup = new MonitorsGroup();
            monitorsGroup.setName("Group1");
            entityManager.persistAndFlush(monitorsGroup);
            screen.setMonitorsGroupForScreens(monitorsGroup);
            entityManager.persistAndFlush(screen);

            List<Screen> found = screenRepository.findByMonitorsGroupForScreens(monitorsGroup);

            assertThat(found.get(0).getLocation())
                    .isEqualTo("Aveiro");
        }

}
