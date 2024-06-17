package deti.uas.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Models.AppUser;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;

@DataJpaTest
class ScheduleRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ScheduleRepository repository;

    @Test
    void whenFindById_thenReturnSchedule() {
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");
        entityManager.persistAndFlush(user);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        entityManager.persistAndFlush(monitorsGroup);

        Schedule schedule = new Schedule(
                null,
                1,
                LocalTime.parse("12:00:00"),
                LocalTime.parse("13:00:00"),
                LocalDate.parse("2024-04-21"),
                LocalDate.parse("2024-04-21"),
                1,
                user,
                user,
                LocalDate.parse("2024-04-21"),
                List.of(),
                List.of()
        );

        entityManager.persistAndFlush(schedule);

        Schedule found = repository.findById(schedule.getId()).orElse(null);

        assertThat(found).isNotNull().isEqualTo(schedule);
    }

    @Test
    void whenFindAll_thenReturnAllSchedules() {
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");
        entityManager.persistAndFlush(user);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        entityManager.persistAndFlush(monitorsGroup);

        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(7);
        schedule1.setCreatedBy(user);
        schedule1.setEndDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartDate(LocalDate.parse("2024-04-21"));
        schedule1.setPriority(1);
        schedule1.setLastEditedBy(user);

        Schedule schedule2 = new Schedule();
        schedule2.setFrequency(5);
        schedule2.setCreatedBy(user);
        schedule2.setEndDate(LocalDate.parse("2024-04-21"));
        schedule2.setStartDate(LocalDate.parse("2024-04-21"));
        schedule2.setPriority(1);
        schedule2.setLastEditedBy(user);

        entityManager.persistAndFlush(schedule1);
        entityManager.persistAndFlush(schedule2);

        List<Schedule> found = repository.findAll();

        assertThat(found).hasSize(2).contains(schedule1, schedule2);
    }
}
