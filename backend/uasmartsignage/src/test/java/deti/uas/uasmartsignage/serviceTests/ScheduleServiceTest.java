package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import deti.uas.uasmartsignage.Models.*;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import deti.uas.uasmartsignage.Repositories.ScheduleRepository;
import deti.uas.uasmartsignage.Services.LogsService;
import deti.uas.uasmartsignage.Services.ScheduleService;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    private ScheduleRepository repository;

    @Mock
    private TemplateGroupService templateGroupService;

    @Mock
    private LogsService logsService;

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
        schedule.setFrequency(10);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        when(repository.findById(1L)).thenReturn(Optional.of(schedule));

        Schedule schedu = service.getScheduleById(1L);
        assertThat(schedu.getCreatedBy().getEmail()).isEqualTo("admin");
        assertThat(schedu.getEndDate()).isEqualTo(LocalDate.parse("2024-04-21"));
    }

    @Test
    void whenServiceGetAllThenRepositoryFindAll(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(5);
        schedule1.setCreatedBy(user);
        schedule1.setEndDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartDate(LocalDate.parse("2024-04-21"));
        schedule1.setPriority(1);
        schedule1.setLastEditedBy(user);

        Schedule schedule2 = new Schedule();
        schedule2.setFrequency(10);
        schedule2.setCreatedBy(user);
        schedule2.setEndDate(LocalDate.parse("2024-04-21"));
        schedule2.setStartDate(LocalDate.parse("2024-04-21"));
        schedule2.setPriority(1);
        schedule2.setLastEditedBy(user);

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
        schedule.setFrequency(5);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        when(repository.save(schedule)).thenReturn(schedule);

        Schedule schedu = service.saveSchedule(schedule);

        assertThat(schedu.getEndDate()).isEqualTo(LocalDate.parse("2024-04-21"));
    }

    @Test
    void whenServiceDeleteThenRepositoryDelete(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(4);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);

        service.deleteSchedule(1L);

        verify(repository, times(1)).deleteById(1L);
        assertFalse(repository.existsById(1L));

    }

    @Test
    void testUpdateMultipleSchedules(){
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setPending(false);
        monitor.setWidth(1);
        monitor.setHeight(1);

        Monitor monitor1 = new Monitor();
        monitor1.setName("monitor1");
        monitor1.setPending(false);
        monitor1.setWidth(12);
        monitor1.setHeight(12);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");
        group.setMonitors(List.of(monitor,monitor1));

        Widget widget = new Widget();
        widget.setName("widget1");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setWidget(widget);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setWidget(widget);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));


        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group1 = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setFrequency(4);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setTemplateGroups(List.of(templateGroup));
        schedule.setLastEditedBy(user);

        Schedule schedule2 = new Schedule();
        schedule2.setId(2L);
        schedule2.setFrequency(4);
        schedule2.setCreatedBy(user);
        schedule2.setEndDate(LocalDate.parse("2024-04-21"));
        schedule2.setStartDate(LocalDate.parse("2024-04-21"));
        schedule2.setPriority(4);
        schedule2.setTemplateGroups(List.of(templateGroup));
        schedule2.setLastEditedBy(user);

        Schedule update = new Schedule();
        update.setId(1L);
        update.setFrequency(5);
        update.setCreatedBy(user);
        update.setEndDate(LocalDate.parse("2024-06-21"));
        update.setStartDate(LocalDate.parse("2024-06-21"));
        update.setPriority(1);
        update.setTemplateGroups(List.of(templateGroup));
        update.setLastEditedBy(user);

        //  List<Schedule> schedules = Arrays.asList(schedule, update);

        when(repository.findById(1L)).thenReturn(Optional.of(schedule));

        when(repository.findById(2L)).thenReturn(Optional.of(schedule2));

        when(repository.save(Mockito.any())).thenReturn(schedule2);

        when(repository.save(Mockito.any())).thenReturn(update);


        List<Schedule> schedules = Arrays.asList(schedule, schedule2);

        List<Schedule> updatedSchedules = service.updateSchedules(schedules);

        assertThat(updatedSchedules).hasSize(2);
        assertThat(updatedSchedules.get(0).getEndDate()).isEqualTo(LocalDate.parse("2024-06-21"));
        assertThat(updatedSchedules.get(0).getFrequency()).isEqualTo(5);
    }

    @Test
    void testUpdateSchedule(){
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setFrequency(4);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);

        Schedule update = new Schedule();
        update.setId(1L);
        update.setFrequency(5);
        update.setCreatedBy(user);
        update.setEndDate(LocalDate.parse("2024-06-21"));
        update.setStartDate(LocalDate.parse("2024-06-21"));
        update.setPriority(1);
        update.setLastEditedBy(user);

        when(repository.save(Mockito.any())).thenReturn(update);

        Schedule updatedSchedule = service.updateSchedule(update);

        assertThat(updatedSchedule.getEndDate()).isEqualTo(LocalDate.parse("2024-06-21"));
        assertThat(updatedSchedule.getFrequency()).isEqualTo(5);
    }


}
