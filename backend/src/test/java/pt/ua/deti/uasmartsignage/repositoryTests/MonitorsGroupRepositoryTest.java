// package pt.ua.deti.uasmartsignage.repositoryTests;

// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.Arrays;
// import java.util.List;
// import java.util.UUID;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.ActiveProfiles;

// import pt.ua.deti.uasmartsignage.models.Monitor;
// import pt.ua.deti.uasmartsignage.models.MonitorGroup;
// import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;
// import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;

// @DataJpaTest
// @ActiveProfiles("test")
// class MonitorGroupRepositoryTest {
    
//     @Autowired
//     private MonitorGroupRepository repository;
//     @Autowired
//     private MonitorRepository monitorRepository;

//     @Test void
//     findAllByMonitorsPendingFalse(){
//         Monitor m1 = new Monitor(null, "m1", 0, 0, UUID.randomUUID().toString(), false,true, null);
//         MonitorGroup g1 = new MonitorGroup(null, "g1", false, "bom grupo", null);
//         repository.save(g1);
//         m1.setGroup(g1);
//         monitorRepository.save(m1);

//         Monitor m2 = new Monitor(null, "m2", 0, 0, UUID.randomUUID().toString(), true,true, null);
//         MonitorGroup g2 = new MonitorGroup(null, "g2", false, "bom grupo 2", null);
//         repository.save(g2);
//         m2.setGroup(g2);
//         monitorRepository.save(m2);

//         List<MonitorGroup> result = repository.findAllByMonitorsIsEmptyOrMonitorsPendingFalse();

//         assertThat(result).hasSize(1).extracting(MonitorGroup::getName).contains("g1");
//     }

//     @Test void
//     findAllByMadeForMonitorFalse(){
//         MonitorGroup g1 = new MonitorGroup(null, "g1", true, "bom grupo", null);
//         MonitorGroup g2 = new MonitorGroup(null, "g2", false, "bom grupo", null);
//         MonitorGroup g3 = new MonitorGroup(null, "g3", false, "bom grupo", null);

//         repository.saveAll(Arrays.asList(g1,g2,g3));

//         List<MonitorGroup> result = repository.findAllByMonitorsIsEmptyOrIsDefaultGroupFalse();

//         assertThat(result).hasSize(2).extracting(MonitorGroup::getName).contains("g2","g3");
//     }
// }
