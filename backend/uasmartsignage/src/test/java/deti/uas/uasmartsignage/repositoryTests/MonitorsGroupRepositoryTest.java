package deti.uas.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;

@DataJpaTest
@ActiveProfiles("test")
class MonitorsGroupRepositoryTest {
    
    @Autowired
    private MonitorGroupRepository repository;
    @Autowired
    private MonitorRepository monitorRepository;

    @Test void
    findAllByMonitorsPendingFalse(){
        Monitor m1 = new Monitor(null, "m1", 0, 0, UUID.randomUUID().toString(), false, null);
        MonitorsGroup g1 = new MonitorsGroup(null, "g1", false, "bom grupo", null, null, null);
        repository.save(g1);
        m1.setGroup(g1);
        monitorRepository.save(m1);

        Monitor m2 = new Monitor(null, "m2", 0, 0, UUID.randomUUID().toString(), true, null);
        MonitorsGroup g2 = new MonitorsGroup(null, "g2", false, "bom grupo 2", null, null,null);
        repository.save(g2);
        m2.setGroup(g2);
        monitorRepository.save(m2);

        List<MonitorsGroup> result = repository.findAllByMonitorsPendingFalse();

        assertThat(result).hasSize(1).extracting(MonitorsGroup::getName).contains("g1");
    }

    @Test void
    findAllByMadeForMonitorFalse(){
        MonitorsGroup g1 = new MonitorsGroup(null, "g1", true, "bom grupo", null, null,null);
        MonitorsGroup g2 = new MonitorsGroup(null, "g2", false, "bom grupo", null, null,null);
        MonitorsGroup g3 = new MonitorsGroup(null, "g3", false, "bom grupo", null, null,null);

        repository.saveAll(Arrays.asList(g1,g2,g3));

        List<MonitorsGroup> result = repository.findAllByMadeForMonitorFalse();

        assertThat(result).hasSize(2).extracting(MonitorsGroup::getName).contains("g2","g3");
    }
}