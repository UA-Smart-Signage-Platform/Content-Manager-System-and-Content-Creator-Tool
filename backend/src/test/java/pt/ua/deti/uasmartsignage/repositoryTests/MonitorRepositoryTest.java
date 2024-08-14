package pt.ua.deti.uasmartsignage.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MonitorRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private MonitorRepository repository;

    @Test void
    whenSavedFindAllGetAllMonitors(){
        MonitorGroup group = new MonitorGroup();
        group.setName("departamento");
        entityManager.persist(group);
        entityManager.flush();

        Monitor deti = new Monitor();
        deti.setUuid("192.178.1");
        deti.setName("deti");
        deti.setPending(false);
        deti.setGroup(group);
        deti = repository.save(deti);

        Monitor hall = new Monitor();
        hall.setUuid("192.178.2");
        hall.setName("hall");
        hall.setPending(false);
        hall.setGroup(group);
        hall = repository.save(hall);

        List<Monitor> list = repository.findAll();

        assertThat(list).hasSize(2).extracting(Monitor::getName).contains("deti","hall");
    }

    @Test void 
    findMonitorsByGroupId(){
        MonitorGroup group = new MonitorGroup();
        group.setName("departamento");
        entityManager.persist(group);
        entityManager.flush();

        MonitorGroup group2 = new MonitorGroup();
        group2.setName("departamento2");
        entityManager.persist(group2);
        entityManager.flush();

        Monitor deti = new Monitor();
        deti.setUuid("192.178.1");
        deti.setName("deti");
        deti.setPending(false);
        deti.setGroup(group);
        deti = repository.save(deti);

        Monitor hall = new Monitor();
        hall.setUuid("192.178.2");
        hall.setName("hall");
        hall.setPending(false);
        hall.setGroup(group);
        hall = repository.save(hall);

        Monitor room = new Monitor();
        room.setName("room");
        room.setUuid("192.178.3");
        room.setPending(false);
        room.setGroup(group2);
        room = repository.save(room);

        List<Monitor> list = repository.findByPendingAndGroup_Id(false,group.getId());

        assertThat(list).hasSize(2).extracting(Monitor::getName).contains("deti","hall");
    }

    @Test void
    findPendingMonitors(){
        MonitorGroup group = new MonitorGroup();
        group.setName("departamento");
        entityManager.persist(group);
        entityManager.flush();

        MonitorGroup group2 = new MonitorGroup();
        group2.setName("departamento2");
        entityManager.persist(group2);
        entityManager.flush();

        Monitor deti = new Monitor();
        deti.setUuid("192.178.1");
        deti.setName("deti");
        deti.setPending(true);
        deti.setGroup(group);
        deti = repository.save(deti);

        Monitor hall = new Monitor();
        hall.setUuid("192.178.2");
        hall.setName("hall");
        hall.setPending(false);
        hall.setGroup(group);
        hall = repository.save(hall);

        Monitor room = new Monitor();
        room.setName("room");
        room.setUuid("192.178.3");
        room.setPending(true);
        room.setGroup(group2);
        room = repository.save(room);

        List<Monitor> list = repository.findByPending(true);

        assertThat(list).hasSize(2).extracting(Monitor::getName).contains("deti","room");
    } 
    @Test void
    findNotPendingMonitors(){
        MonitorGroup group = new MonitorGroup();
        group.setName("departamento");
        entityManager.persist(group);
        entityManager.flush();

        MonitorGroup group2 = new MonitorGroup();
        group2.setName("departamento2");
        entityManager.persist(group2);
        entityManager.flush();

        Monitor deti = new Monitor();
        deti.setUuid("192.178.1");
        deti.setName("deti");
        deti.setPending(true);
        deti.setGroup(group);
        deti = repository.save(deti);

        Monitor hall = new Monitor();
        hall.setUuid("192.178.2");
        hall.setName("hall");
        hall.setPending(false);
        hall.setGroup(group);
        hall = repository.save(hall);

        Monitor room = new Monitor();
        room.setName("room");
        room.setUuid("192.178.3");
        room.setPending(true);
        room.setGroup(group2);
        room = repository.save(room);

        List<Monitor> list = repository.findByPending(false);

        assertThat(list).hasSize(1).extracting(Monitor::getName).contains("hall");
    }

    @Test void
    removeMonitor(){
        MonitorGroup group = new MonitorGroup();
        group.setName("departamento");
        entityManager.persist(group);
        entityManager.flush();

        Monitor deti = new Monitor();
        deti.setUuid("192.178.1");
        deti.setName("deti");
        deti.setPending(true);
        deti.setGroup(group);
        entityManager.persist(deti);
        entityManager.flush();

        List<Monitor> listBefore = repository.findAll();

        repository.delete(deti);

        List<Monitor> list = repository.findAll();

        assertThat(listBefore).hasSize(1);
        assertThat(list).hasSize(0);

    }
}
