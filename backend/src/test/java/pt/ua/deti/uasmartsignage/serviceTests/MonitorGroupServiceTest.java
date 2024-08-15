// package pt.ua.deti.uasmartsignage.serviceTests;


// import pt.ua.deti.uasmartsignage.models.Monitor;
// import pt.ua.deti.uasmartsignage.models.MonitorGroup;
// import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;
// import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
// import pt.ua.deti.uasmartsignage.services.LogsService;
// import pt.ua.deti.uasmartsignage.services.MonitorGroupService;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import static org.assertj.core.api.Assertions.assertThat;


// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class MonitorGroupServiceTest {

//     @Mock
//     private MonitorGroupRepository monitorGroupRepository;

//     @Mock
//     private MonitorRepository monitorRepository;

//     @Mock
//     private LogsService logsService;

//     @InjectMocks
//     private MonitorGroupService service;


//     @Test
//     void whenGetGroupById_thenReturnGroup() {
//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);

//         when(monitorGroupRepository.findById(1L)).thenReturn(Optional.of(monitorsGroup));

//         MonitorGroup found = service.getGroupById(1L);

//         assertThat(found).isEqualTo(monitorsGroup);
//         verify(monitorGroupRepository, times(1)).findById(1L);
//     }

//     @Test
//     void whenSaveGroup_thenGroupIsSaved() {
//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);

//         when(monitorGroupRepository.save(monitorsGroup)).thenReturn(monitorsGroup);

//         MonitorGroup saved = service.saveGroup(monitorsGroup);

//         assertThat(saved).isEqualTo(monitorsGroup);
//     }

//     @Test
//     void whenDeleteGroup_thenGroupIsDeleted() {
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);
//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);
//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);
//         monitorsGroup.setMonitors(java.util.Arrays.asList(monitor, monitor2));

//         when(monitorGroupRepository.findById(1L)).thenReturn(Optional.of(monitorsGroup));

//         service.deleteGroupById(1L);

//         //check if monitors are saved with the new group
//         verify(monitorGroupRepository, times(1)).deleteById(1L);
//         verify(monitorRepository, times(1)).save(monitor);
//         verify(monitorRepository, times(1)).save(monitor2);
//         verify(monitorGroupRepository, times(2)).save(any(MonitorGroup.class));
//     }

//     @Test
//     void whenGetAllGroups_thenReturnAllGroups() {
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);
//         monitor.setPending(false);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);
//         monitor2.setPending(true);

//         Monitor monitor3 = new Monitor();
//         monitor3.setName("monitor3");
//         monitor3.setId(3L);
//         monitor3.setPending(false);

//         // OK
//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);
//         monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));

//         // Still Pending
//         MonitorGroup monitorsGroup2 = new MonitorGroup();
//         monitorsGroup2.setName("group2");
//         monitorsGroup2.setId(2L);
//         monitorsGroup2.setMonitors(List.of(monitor2));

//         // Test purposes this type of group will not be available in the application (madeForMonitor = true) with multiple monitors
//         MonitorGroup monitorsGroup3 = new MonitorGroup();
//         monitorsGroup3.setName("group3");
//         monitorsGroup3.setId(3L);
//         monitorsGroup3.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

//         when(monitorGroupRepository.findAllByMonitorsIsEmptyOrMonitorsPendingFalse()).thenReturn(List.of(monitorsGroup));

//         List<MonitorGroup> found = service.getAllGroups();

//         assertThat(found).isEqualTo(List.of(monitorsGroup));
//         verify(monitorGroupRepository, times(1)).findAllByMonitorsIsEmptyOrMonitorsPendingFalse();
//     }

//     @Test
//     void whenGetAllGroupsNotMadeForMonitor_thenReturnAllGroupsNotMadeForMonitor () {
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         Monitor monitor3 = new Monitor();
//         monitor3.setName("monitor3");
//         monitor3.setId(3L);

//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);
//         monitorsGroup.setDefaultGroup(false);
//         monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));

//         MonitorGroup monitorsGroup2 = new MonitorGroup();
//         monitorsGroup2.setName("group2");
//         monitorsGroup2.setId(2L);
//         monitorsGroup2.setDefaultGroup(true);
//         monitorsGroup2.setMonitors(List.of(monitor2));

//         MonitorGroup monitorsGroup3 = new MonitorGroup();
//         monitorsGroup3.setName("group3");
//         monitorsGroup3.setId(3L);
//         monitorsGroup3.setDefaultGroup(false);
//         monitorsGroup3.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

//         when(monitorGroupRepository.findAllByMonitorsIsEmptyOrIsDefaultGroupFalse()).thenReturn(Arrays.asList(monitorsGroup, monitorsGroup3));

//         List<MonitorGroup> found = service.getAllNonDefaultGroups();

//         assertThat(found).isEqualTo(Arrays.asList(monitorsGroup,monitorsGroup3));
//         verify(monitorGroupRepository, times(1)).findAllByMonitorsIsEmptyOrIsDefaultGroupFalse();
//     }

//     @Test
//     void testGetGroupByName(){
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         Monitor monitor3 = new Monitor();
//         monitor3.setName("monitor3");
//         monitor3.setId(3L);

//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);
//         monitorsGroup.setDefaultGroup(false);
//         monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));
//         when(monitorGroupRepository.findByName("group1")).thenReturn(monitorsGroup);

//         MonitorGroup found = service.getGroupByName("group1");

//         assertThat(found).isEqualTo(monitorsGroup);
//         verify(monitorGroupRepository, times(1)).findByName("group1");
//     }



//     @Test
//     void testUpdateGroup(){
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         Monitor monitor3 = new Monitor();
//         monitor3.setName("monitor3");
//         monitor3.setId(3L);

//         MonitorGroup monitorsGroup = new MonitorGroup();
//         monitorsGroup.setName("group1");
//         monitorsGroup.setId(1L);
//         monitorsGroup.setDefaultGroup(false);
//         monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));

//         MonitorGroup monitorsGroupUpdated = new MonitorGroup();
//         monitorsGroupUpdated.setName("group2");
//         monitorsGroupUpdated.setId(1L);
//         monitorsGroupUpdated.setDefaultGroup(false);
//         monitorsGroupUpdated.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

//         when(monitorGroupRepository.getReferenceById(1L)).thenReturn(monitorsGroup);
//         when(monitorGroupRepository.save(monitorsGroup)).thenReturn(monitorsGroupUpdated);


//         MonitorGroup retu = service.updateGroup(1L, monitorsGroupUpdated);

//         assertThat(retu.getName()).isEqualTo("group2");
//         assertThat(retu.getMonitors()).hasSize(3);
//         verify(monitorGroupRepository, times(1)).save(monitorsGroup);


//     }




// }