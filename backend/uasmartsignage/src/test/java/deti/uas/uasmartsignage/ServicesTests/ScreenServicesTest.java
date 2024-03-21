package deti.uas.uasmartsignage.ServicesTests;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Services.MonitorService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ScreenServicesTest {

    @Mock( lenient = true)
    private MonitorRepository screenRepository;

    @InjectMocks
    private MonitorService screenService;

    @BeforeEach
    public void setUp(){
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        Monitor screen = new Monitor();
        screen.setLocation("Aveiro");
        screen.setStatus(true);
        screen.setMonitorsGroupForScreens(monitorsGroup);
        Monitor screen2 = new Monitor();
        screen2.setLocation("Porto");
        screen2.setStatus(false);
        screen2.setMonitorsGroupForScreens(monitorsGroup);
        List<Monitor> allScreens = Arrays.asList(screen, screen2);
        Mockito.when(screenRepository.findByLocation(screen.getLocation())).thenReturn(screen);
        Mockito.when(screenRepository.findById(1L)).thenReturn(Optional.of(screen));
        Mockito.when(screenRepository.findAll()).thenReturn(allScreens);
        Mockito.when(screenRepository.save(screen)).thenReturn(screen);
        Mockito.when(screenRepository.findByMonitorsGroupForScreens(monitorsGroup)).thenReturn(allScreens);
    }

    @Test
    public void whenValidGroup_thenScreenShouldBeFound() {
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        Monitor screen = new Monitor();
        screen.setLocation("Aveiro");
        screen.setStatus(true);
        screen.setMonitorsGroupForScreens(monitorsGroup);
        Monitor screen2 = new Monitor();
        screen2.setLocation("Porto");
        screen2.setStatus(false);
        screen2.setMonitorsGroupForScreens(monitorsGroup);
        List<Monitor> allScreens = Arrays.asList(screen, screen2);
        Mockito.when(screenRepository.findByMonitorsGroupForScreens(monitorsGroup)).thenReturn(allScreens);
        List<Monitor> found = screenService.getScreensByGroup(monitorsGroup);
        assertThat(found.size()).isEqualTo(2);
    }

    @Test
    public void whenValidId_thenScreenShouldBeFound() {
        Monitor found = screenService.getScreenById(1L);
        assertThat(found.getLocation()).isEqualTo("Aveiro");
    }

}
