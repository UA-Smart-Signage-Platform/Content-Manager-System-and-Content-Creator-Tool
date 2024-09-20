package pt.ua.deti.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import pt.ua.deti.uasmartsignage.services.LogsService;
import pt.ua.deti.uasmartsignage.services.MonitorGroupService;
import pt.ua.deti.uasmartsignage.services.MonitorService;
import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;

@ExtendWith(MockitoExtension.class)
class MonitorServiceTest {

    @Mock
    private MonitorRepository repository;

    @Mock
    private LogsService logsService;

    @InjectMocks
    private MonitorService service;

    @Mock
    private MonitorGroupService groupService;

    @Mock
    private MonitorGroupRepository MonitorGroupRepository;

    @Test void
    getMonitorByIdTestReturnsMonitor(){
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(false);
        when(repository.findById(1L)).thenReturn(Optional.of(monitor));

        Monitor retu = service.getMonitorById(1L).get();

        assertThat(retu.getName()).isEqualTo("monitor");
    }

    @Test void
    getMonitorByIdTestReturnsNull(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Monitor> retu = service.getMonitorById(1L);

        assertThat(retu).isEmpty();
    }

    @Test void
    whenServiceSaveThenRepositorySave(){
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(false);
        when(repository.save(monitor)).thenReturn(monitor);

        Monitor retu = service.saveMonitor(monitor);

        assertThat(retu.getName()).isEqualTo("monitor");
    }

    @Test void
    UpdateMonitorServiceTest(){
        MonitorGroup group1 = new MonitorGroup();
        group1.setId(1L);
        group1.setName("group1");

        MonitorGroup group2 = new MonitorGroup();
        group2.setId(2L);
        group2.setName("group2");

        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setGroup(group1);
        monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(false);

        Monitor monitorUpdated = new Monitor();
        monitorUpdated.setGroup(group2);
        monitorUpdated.setName("Name");
        monitorUpdated.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitorUpdated.setPending(false);

        when(repository.findById(1L)).thenReturn(Optional.of(monitor));
        when(groupService.getGroupById(2L)).thenReturn(Optional.of(group2));
        when(repository.save(any())).thenReturn(monitorUpdated);

        Monitor retu = service.updateMonitor(1L, monitorUpdated);

        assertThat(retu.getName()).isEqualTo("Name");
        assertThat(retu.getGroup().getName()).isEqualTo("group2");

    }
    
    @Test void
    whenUpdatePendingInServiceThenPendingIsUpdated(){
        MonitorGroup group2 = new MonitorGroup();
        group2.setId(2L);
        group2.setName("group2");

        Monitor monitor = new Monitor();
        monitor.setId(1L);
        monitor.setName("monitor");
        monitor.setGroup(group2);
        monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(true);

        when(repository.save(monitor)).thenReturn(monitor);
        when(repository.getReferenceById(1L)).thenReturn(monitor);

        Monitor retu = service.updatePending(1L, false);

        assertFalse(retu.isPending());
    }

    @Test void
    WhenServiceGetAllMonitorsByPendingThenRepositoryGetAllMonitorsByPending(){
        MonitorGroup group1 = new MonitorGroup();
        group1.setId(1L);
        group1.setName("group1");

        MonitorGroup group2 = new MonitorGroup();
        group2.setId(2L);
        group2.setName("group2");

        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setGroup(group1);
        monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setGroup(group2);
        monitor2.setName("Name");
        monitor2.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor2.setPending(false);

        when(repository.findByPending(false)).thenReturn(Arrays.asList(monitor,monitor2));

        List<Monitor> monitors = service.getAllMonitorsByPending(false);

        assertThat(monitors).hasSize(2).extracting(Monitor::getName).contains("monitor","Name");
    }

    @Test void
    whenServiceGetMonitorsByGroupThenRepositoryGetMonitorsByGroupNotPending(){
        MonitorGroup group1 = new MonitorGroup();
        group1.setId(1L);
        group1.setName("group2");

        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setGroup(group1);
        monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setGroup(group1);
        monitor2.setName("Name");
        monitor2.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor2.setPending(false);

        when(repository.findByPendingAndGroup_Id(false, 1L)).thenReturn(Arrays.asList(monitor,monitor2));

        List<Monitor> monitors = service.getMonitorsByGroup(1L);

        assertThat(monitors).hasSize(2).extracting(Monitor::getName).contains("monitor","Name");
    }

    @Test
    public void whenServiceDeleteMonitorThenReturnTrue() {
        Long monitorId = 1L;
        Monitor monitor = new Monitor();
        when(repository.findById(monitorId)).thenReturn(Optional.of(monitor));

        Boolean result = service.deleteMonitor(monitorId);

        assertTrue(result);
        verify(repository, times(1)).deleteById(monitorId);
    }

    @Test
    public void whenServiceDeleteMonitorThenReturnFalse() {
        Long monitorId = 1L;
        when(repository.findById(monitorId)).thenReturn(Optional.empty());

        Boolean result = service.deleteMonitor(monitorId);

        assertFalse(result);
        verify(repository, never()).deleteById(monitorId);
        verify(logsService, times(1)).addLogEntry(eq(Severity.ERROR), any(), any(), any(), any());
    }

    @Test
    public void whenServicetestGetMonitorByUUIDThenReturnMonitor() {
        String uuid = "test-uuid";
        Monitor expectedMonitor = new Monitor();
        when(repository.findByUuid(uuid)).thenReturn(expectedMonitor);

        Monitor result = service.getMonitorByUUID(uuid);

        assertEquals(expectedMonitor, result);
    }

    @Test
    public void testGetAllMonitorsByPendingAndOnline() {
        boolean pending = true;
        boolean onlineStatus = false;
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        List<Monitor> monitors = new ArrayList<>();
        monitors.add(monitor1);
        monitors.add(monitor2);

        when(repository.findByPendingAndOnline(pending, onlineStatus)).thenReturn(monitors);
        when(logsService.keepAliveIn10min(monitor1)).thenReturn(true);
        when(logsService.keepAliveIn10min(monitor2)).thenReturn(false);

        List<Monitor> result = service.getAllMonitorsByPendingAndOnline(pending, onlineStatus);

        assertEquals(2, result.size());
        assertTrue(monitor1.isOnline());
        assertFalse(monitor2.isOnline());
        verify(repository, times(1)).findByPendingAndOnline(pending, onlineStatus);
        verify(logsService, times(1)).keepAliveIn10min(monitor1);
        verify(logsService, times(1)).keepAliveIn10min(monitor2);
    }

    @Test
    public void whenGetMonitorsByGroupAndOnlineThenReturnList() {
        long groupId = 1L;
        boolean onlineStatus = true;
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        List<Monitor> monitors = new ArrayList<>();
        monitors.add(monitor1);
        monitors.add(monitor2);
        
        when(repository.findByPendingAndGroup_IdAndOnline(false, groupId, onlineStatus)).thenReturn(monitors);
        when(logsService.keepAliveIn10min(monitor1)).thenReturn(true);
        when(logsService.keepAliveIn10min(monitor2)).thenReturn(false);

        List<Monitor> result = service.getMonitorsByGroupAndOnline(groupId, onlineStatus);

        assertEquals(2, result.size());
        assertTrue(monitor1.isOnline());
        assertFalse(monitor2.isOnline());
        verify(repository, times(1)).findByPendingAndGroup_IdAndOnline(false, groupId, onlineStatus);
        verify(logsService, times(1)).keepAliveIn10min(monitor1);
        verify(logsService, times(1)).keepAliveIn10min(monitor2);
    }
}
