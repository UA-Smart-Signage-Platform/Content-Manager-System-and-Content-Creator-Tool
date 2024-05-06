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
import deti.uas.uasmartsignage.Models.User;
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
        User user = new User();
        user.setUsername("admin");
        user.setRole(1);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(10);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));

        Schedule schedu = service.getScheduleById(1L);
        assertThat(schedu.getCreatedBy().getUsername()).isEqualTo("admin");
        assertThat(schedu.getEndDate()).isEqualTo(LocalDate.parse("2024-04-21"));
    }

    @Test
    void whenServiceGetAllThenRepositoryFindAll(){
        User user = new User();
        user.setUsername("admin");
        user.setRole(1);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(5);
        schedule1.setCreatedBy(user);
        schedule1.setEndDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartDate(LocalDate.parse("2024-04-21"));
        schedule1.setPriority(1);
        schedule1.setLastEditedBy(user);
        schedule1.setMonitorsGroupForSchedules(group);

        Schedule schedule2 = new Schedule();
        schedule2.setFrequency(10);
        schedule2.setCreatedBy(user);
        schedule2.setEndDate(LocalDate.parse("2024-04-21"));
        schedule2.setStartDate(LocalDate.parse("2024-04-21"));
        schedule2.setPriority(1);
        schedule2.setLastEditedBy(user);
        schedule2.setMonitorsGroupForSchedules(group);

        when(repository.findAll()).thenReturn(Arrays.asList(schedule1, schedule2));

        List<Schedule> schedules = (List<Schedule>) service.getAllSchedules();

        assertThat(schedules).hasSize(2).contains(schedule1, schedule2);
    }

    @Test void
    whenServiceSaveThenRepositorySave(){
        User user = new User();
        user.setUsername("admin");
        user.setRole(1);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(5);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);
        when(repository.save(schedule)).thenReturn(schedule);

        Schedule schedu = service.saveSchedule(schedule);

        assertThat(schedu.getEndDate()).isEqualTo(LocalDate.parse("2024-04-21"));
    }

    @Test
    void whenServiceDeleteThenRepositoryDelete(){
        User user = new User();
        user.setUsername("admin");
        user.setRole(1);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(4);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);

        service.deleteSchedule(1L);

        verify(repository, times(1)).deleteById(1L);
        assertFalse(repository.existsById(1L));

    }
    //missing update(not know what can be updated)

}
