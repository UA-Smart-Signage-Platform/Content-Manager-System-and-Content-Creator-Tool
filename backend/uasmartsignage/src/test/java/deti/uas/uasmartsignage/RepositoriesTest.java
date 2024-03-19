package deti.uas.uasmartsignage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import deti.uas.uasmartsignage.Repositories.ScreenRepository;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.Screen;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoriesTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private ScreenRepository screenRepository;

        @Autowired
        private MonitorGroupRepository monitorGroupRepository;

        @Autowired
        private TemplateRepository templateRepository;

        // ScreenRepository tests
        @Test
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

        // MonitorGroupRepository tests
        @Test
        public void whenFindByName_thenReturnMonitorsGroup() {
            MonitorsGroup monitorsGroup = new MonitorsGroup();
            monitorsGroup.setName("Group1");
            entityManager.persistAndFlush(monitorsGroup);

            MonitorsGroup found = monitorGroupRepository.findByName(monitorsGroup.getName());

            assertThat(found.getName())
                    .isEqualTo("Group1");
        }

        @Test
        public void whenFindById_thenReturnMonitorsGroup() {
            MonitorsGroup monitorsGroup = new MonitorsGroup();
            monitorsGroup.setName("Group2");
            entityManager.persistAndFlush(monitorsGroup);

            MonitorsGroup found = monitorGroupRepository.findById(monitorsGroup.getId()).orElse(null);

            assertThat(found.getName())
                    .isEqualTo("Group2");
        }

        // TemplateRepository tests
        @Test
        public void whenFindById_thenReturnTemplate() {
            Template template = new Template();
            template.setName("Template1");
            template.setPath("path");
            entityManager.persistAndFlush(template);

            Template found = templateRepository.findById(template.getId()).orElse(null);

            assertThat(found.getName())
                    .isEqualTo("Template1");
        }


}
