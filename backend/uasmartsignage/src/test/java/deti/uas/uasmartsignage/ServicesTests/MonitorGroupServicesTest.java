package deti.uas.uasmartsignage.ServicesTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import deti.uas.uasmartsignage.Models.Screen;
import deti.uas.uasmartsignage.Models.TemplateGroup;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MonitorGroupServicesTest {
    @Mock( lenient = true)
    private MonitorGroupRepository monitorGroupRepository;


    @InjectMocks
    private MonitorGroupService monitorGroupService;

    @BeforeEach
    public void setUp(){

        Screen screen = new Screen();
        screen.setLocation("Aveiro");
        TemplateGroup templateGroup = new TemplateGroup();
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        monitorsGroup.setScreens(List.of(screen));
        monitorsGroup.setTemplateGroup(templateGroup);

        MonitorsGroup monitorsGroup2 = new MonitorsGroup();
        monitorsGroup2.setName("Group2");
        monitorsGroup2.setScreens(List.of(screen));

        List<MonitorsGroup> allGroups = Arrays.asList(monitorsGroup, monitorsGroup2);

        Mockito.when(monitorGroupRepository.findByName(monitorsGroup.getName())).thenReturn(monitorsGroup);
        Mockito.when(monitorGroupRepository.findById(1L)).thenReturn(Optional.of(monitorsGroup));
        Mockito.when(monitorGroupRepository.findAll()).thenReturn(allGroups);
        Mockito.when(monitorGroupRepository.save(monitorsGroup)).thenReturn(monitorsGroup);


    }

    @Test
    public void whenValidId_thenGroupShouldBeFound() {
        MonitorsGroup fromDb = monitorGroupService.getGroupById(1L);
        System.out.println("ygeuhgioejio" + fromDb);
        assertThat(fromDb.getName()).isEqualTo("Group1");
    }

    @Test
    public void whenValidObject_saveGroup() {
        Screen screen = new Screen();
        screen.setLocation("Aveiro");
        TemplateGroup templateGroup = new TemplateGroup();
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group2");
        monitorsGroup.setScreens(List.of(screen));
        monitorsGroup.setTemplateGroup(templateGroup);
        Mockito.when(monitorGroupRepository.save(monitorsGroup)).thenReturn(monitorsGroup);
        MonitorsGroup saved = monitorGroupService.saveGroup(monitorsGroup);
        assertThat(saved).isEqualTo(monitorsGroup);
    }

    @Test
    public void whenValidId_deleteGroup() {
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group2");
        monitorGroupService.deleteGroup(2L);
        Mockito.verify(monitorGroupRepository, VerificationModeFactory.times(1)).deleteById(2L);
    }

    @Test
    public void whenValidName_thenGroupShouldBeFound() {
        MonitorsGroup fromDb = monitorGroupService.getGroupByName("Group1");
        assertThat(fromDb.getName()).isEqualTo("Group1");
    }

    @Test
    public void getAllGroups() {
        Screen screen = new Screen();
        screen.setLocation("Aveiro");
        TemplateGroup templateGroup = new TemplateGroup();
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("Group1");
        monitorsGroup.setScreens(List.of(screen));
        monitorsGroup.setTemplateGroup(templateGroup);

        MonitorsGroup monitorsGroup2 = new MonitorsGroup();
        monitorsGroup2.setName("Group2");
        monitorsGroup2.setScreens(List.of(screen));

        List<MonitorsGroup> allGroups = Arrays.asList(monitorsGroup, monitorsGroup2);
        assertThat(allGroups).hasSize(2).extracting(MonitorsGroup::getName).contains(monitorsGroup.getName(), monitorsGroup2.getName());
    }


}
