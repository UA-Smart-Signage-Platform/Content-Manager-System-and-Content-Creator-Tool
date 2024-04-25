package deti.uas.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Models.User;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;

@DataJpaTest
class ScheduleRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ScheduleRepository repository;

    @Test
    void whenFindById_thenReturnSchedule() {
        User user = new User();
        user.setUsername("admin");
        user.setRole(1);
        entityManager.persistAndFlush(user);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        entityManager.persistAndFlush(monitorsGroup);

        Schedule schedule = new Schedule();
        schedule.setDate(LocalDate.parse("2021-06-01"));
        schedule.setFrequency("daily");
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule.setStartDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule.setPriority(1);
        schedule.setNTimes(10);
        schedule.setIntervalOfTime(10);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(monitorsGroup);

        entityManager.persistAndFlush(schedule);

        Schedule found = repository.findById(schedule.getId()).get();

        assertThat(found).isEqualTo(schedule);
    }

    @Test
    void whenFindAll_thenReturnAllSchedules() {
        User user = new User();
        user.setUsername("admin");
        user.setRole(1);
        entityManager.persistAndFlush(user);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        entityManager.persistAndFlush(monitorsGroup);

        Schedule schedule1 = new Schedule();
        schedule1.setDate(LocalDate.parse("2021-06-01"));
        schedule1.setFrequency("weekly");
        schedule1.setCreatedBy(user);
        schedule1.setEndDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule1.setStartDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule1.setPriority(1);
        schedule1.setNTimes(10);
        schedule1.setIntervalOfTime(10);
        schedule1.setLastEditedBy(user);
        schedule1.setMonitorsGroupForSchedules(monitorsGroup);

        Schedule schedule2 = new Schedule();
        schedule2.setDate(LocalDate.parse("2021-06-01"));
        schedule2.setFrequency("daily");
        schedule2.setCreatedBy(user);
        schedule2.setEndDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule2.setStartDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule2.setPriority(1);
        schedule2.setNTimes(10);
        schedule2.setIntervalOfTime(10);
        schedule2.setLastEditedBy(user);
        schedule2.setMonitorsGroupForSchedules(monitorsGroup);

        entityManager.persistAndFlush(schedule1);
        entityManager.persistAndFlush(schedule2);

        List<Schedule> found = repository.findAll();

        assertThat(found).hasSize(2).contains(schedule1, schedule2);
    }
}
