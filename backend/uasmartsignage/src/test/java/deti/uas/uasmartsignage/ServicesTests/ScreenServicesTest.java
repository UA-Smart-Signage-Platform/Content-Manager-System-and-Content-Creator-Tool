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
import deti.uas.uasmartsignage.Models.Screen;
import deti.uas.uasmartsignage.Repositories.ScreenRepository;
import deti.uas.uasmartsignage.Services.ScreenService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ScreenServicesTest {

    @Mock( lenient = true)
    private ScreenRepository screenRepository;

    @InjectMocks
    private ScreenService screenService;

    @BeforeEach
    public void setUp(){
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        Screen screen = new Screen();
        screen.setLocation("Aveiro");
        screen.setStatus(true);
        screen.setMonitorsGroupForScreens(monitorsGroup);
        Screen screen2 = new Screen();
        screen2.setLocation("Porto");
        screen2.setStatus(false);
        screen2.setMonitorsGroupForScreens(monitorsGroup);
        List<Screen> allScreens = Arrays.asList(screen, screen2);
        Mockito.when(screenRepository.findByLocation(screen.getLocation())).thenReturn(screen);
        Mockito.when(screenRepository.findById(1L)).thenReturn(Optional.of(screen));
        Mockito.when(screenRepository.findAll()).thenReturn(allScreens);
        Mockito.when(screenRepository.save(screen)).thenReturn(screen);
        Mockito.when(screenRepository.findByMonitorsGroupForScreens(monitorsGroup)).thenReturn(allScreens);
    }

    @Test
    public void whenValidGroup_thenScreenShouldBeFound() {
        MonitorsGroup monitorsGroup = new MonitorsGroup();
        Screen screen = new Screen();
        screen.setLocation("Aveiro");
        screen.setStatus(true);
        screen.setMonitorsGroupForScreens(monitorsGroup);
        Screen screen2 = new Screen();
        screen2.setLocation("Porto");
        screen2.setStatus(false);
        screen2.setMonitorsGroupForScreens(monitorsGroup);
        List<Screen> allScreens = Arrays.asList(screen, screen2);
        Mockito.when(screenRepository.findByMonitorsGroupForScreens(monitorsGroup)).thenReturn(allScreens);
        List<Screen> found = screenService.getScreensByGroup(monitorsGroup);
        assertThat(found.size()).isEqualTo(2);
    }

    @Test
    public void whenValidId_thenScreenShouldBeFound() {
        Screen found = screenService.getScreenById(1L);
        assertThat(found.getLocation()).isEqualTo("Aveiro");
    }

}
