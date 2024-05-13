package deti.uas.uasmartsignage.serviceTests;


import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Services.LogsService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitorGroupTest {

    @Mock
    private MonitorGroupRepository monitorGroupRepository;

    @Mock
    private MonitorRepository monitorRepository;

    @Mock
    private LogsService logsService;

    @InjectMocks
    private MonitorGroupService service;


    @Test
    void whenGetGroupById_thenReturnGroup() {
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);

        when(monitorGroupRepository.findById(1L)).thenReturn(Optional.of(monitorsGroup));

        MonitorsGroup found = service.getGroupById(1L);

        assertThat(found).isEqualTo(monitorsGroup);
        verify(monitorGroupRepository, times(1)).findById(1L);
    }

    @Test
    void whenSaveGroup_thenGroupIsSaved() {
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);

        when(monitorGroupRepository.save(monitorsGroup)).thenReturn(monitorsGroup);

        MonitorsGroup saved = service.saveGroup(monitorsGroup);

        assertThat(saved).isEqualTo(monitorsGroup);
    }

    @Test
    void whenDeleteGroup_thenGroupIsDeleted() {
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);
        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMonitors(java.util.Arrays.asList(monitor, monitor2));

        when(monitorGroupRepository.findById(1L)).thenReturn(Optional.of(monitorsGroup));

        service.deleteGroup(1L);

        //check if monitors are saved with the new group
        verify(monitorGroupRepository, times(1)).deleteById(1L);
        verify(monitorRepository, times(1)).save(monitor);
        verify(monitorRepository, times(1)).save(monitor2);
        verify(monitorGroupRepository, times(2)).save(any(MonitorsGroup.class));
    }

    @Test
    void whenGetAllGroups_thenReturnAllGroups() {
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);
        monitor2.setPending(true);

        Monitor monitor3 = new Monitor();
        monitor3.setName("monitor3");
        monitor3.setId(3L);
        monitor3.setPending(false);

        // OK
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));

        // Still Pending
        MonitorsGroup monitorsGroup2 = new MonitorsGroup();
        monitorsGroup2.setName("group2");
        monitorsGroup2.setId(2L);
        monitorsGroup2.setMonitors(List.of(monitor2));

        // Test purposes this type of group will not be available in the application (madeForMonitor = true) with multiple monitors
        MonitorsGroup monitorsGroup3 = new MonitorsGroup();
        monitorsGroup3.setName("group3");
        monitorsGroup3.setId(3L);
        monitorsGroup3.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

        when(monitorGroupRepository.findAllByMonitorsPendingFalse()).thenReturn(List.of(monitorsGroup));

        List<MonitorsGroup> found = service.getAllGroups();

        assertThat(found).isEqualTo(List.of(monitorsGroup));
        verify(monitorGroupRepository, times(1)).findAllByMonitorsPendingFalse();
    }

    @Test
    void whenGetAllGroupsNotMadeForMonitor_thenReturnAllGroupsNotMadeForMonitor () {
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        Monitor monitor3 = new Monitor();
        monitor3.setName("monitor3");
        monitor3.setId(3L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));

        MonitorsGroup monitorsGroup2 = new MonitorsGroup();
        monitorsGroup2.setName("group2");
        monitorsGroup2.setId(2L);
        monitorsGroup2.setMadeForMonitor(true);
        monitorsGroup2.setMonitors(List.of(monitor2));

        MonitorsGroup monitorsGroup3 = new MonitorsGroup();
        monitorsGroup3.setName("group3");
        monitorsGroup3.setId(3L);
        monitorsGroup3.setMadeForMonitor(false);
        monitorsGroup3.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

        when(monitorGroupRepository.findAllByMadeForMonitorFalse()).thenReturn(Arrays.asList(monitorsGroup, monitorsGroup3));

        List<MonitorsGroup> found = service.getAllGroupsNotMadeForMonitor();

        assertThat(found).isEqualTo(Arrays.asList(monitorsGroup,monitorsGroup3));
        verify(monitorGroupRepository, times(1)).findAllByMadeForMonitorFalse();
    }



}
