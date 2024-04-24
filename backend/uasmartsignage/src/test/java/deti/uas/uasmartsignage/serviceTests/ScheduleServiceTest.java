package deti.uas.uasmartsignage.serviceTests;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalDateTime;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Models.AppUser;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;
import deti.uas.uasmartsignage.Services.ScheduleService;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    private ScheduleRepository repository;

    @InjectMocks
    private ScheduleService service;

    @Test void
    getScheduleByIdTestReturnsSchedule(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setDate(LocalDate.parse("2021-06-01"));
        schedule.setFrequency("daily");
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule.setStartDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule.setPriority(1);
        schedule.setNTimes(10);
        schedule.setIntervalOfTime(10);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));

        Schedule schedu = service.getScheduleById(1L);

        assertThat(schedu.getFrequency()).isEqualTo("daily");
        assertThat(schedu.getCreatedBy().getEmail()).isEqualTo("admin");
        assertThat(schedu.getEndDate()).isEqualTo(LocalDateTime.parse("2024-04-21T14:00:00"));
    }

    @Test
    void whenServiceGetAllThenRepositoryFindAll(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule1 = new Schedule();
        schedule1.setDate(LocalDate.parse("2021-06-01"));
        schedule1.setFrequency("daily");
        schedule1.setCreatedBy(user);
        schedule1.setEndDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule1.setStartDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule1.setPriority(1);
        schedule1.setNTimes(10);
        schedule1.setIntervalOfTime(10);
        schedule1.setLastEditedBy(user);
        schedule1.setMonitorsGroupForSchedules(group);

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
        schedule2.setMonitorsGroupForSchedules(group);

        when(repository.findAll()).thenReturn(Arrays.asList(schedule1, schedule2));

        List<Schedule> schedules = (List<Schedule>) service.getAllSchedules();

        assertThat(schedules).hasSize(2).contains(schedule1, schedule2);
    }

    @Test void
    whenServiceSaveThenRepositorySave(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setDate(LocalDate.parse("2021-06-01"));
        schedule.setFrequency("weekly");
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule.setStartDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule.setPriority(1);
        schedule.setNTimes(10);
        schedule.setIntervalOfTime(10);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);
        when(repository.save(schedule)).thenReturn(schedule);

        Schedule schedu = service.saveSchedule(schedule);

        assertThat(schedu.getFrequency()).isEqualTo("weekly");
    }

    @Test
    void whenServiceDeleteThenRepositoryDelete(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setDate(LocalDate.parse("2021-06-01"));
        schedule.setFrequency("weekly");
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDateTime.parse("2024-04-21T14:00:00"));
        schedule.setStartDate(LocalDateTime.parse("2024-04-21T12:00:00"));
        schedule.setPriority(1);
        schedule.setNTimes(10);
        schedule.setIntervalOfTime(10);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);

        service.deleteSchedule(1L);

        verify(repository, times(1)).deleteById(1L);
        assertFalse(repository.existsById(1L));

    }

    //missing update(not know what can be updated)

}
