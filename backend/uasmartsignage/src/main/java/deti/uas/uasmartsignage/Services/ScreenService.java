package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.ScreenRepository;
import deti.uas.uasmartsignage.Models.Screen;

import java.util.List;

@Service 
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public Screen getScreenById(Long id) {
        return screenRepository.findById(id).orElse(null);
    }

    public Screen saveScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    public void deleteScreen(Long id) {
        screenRepository.deleteById(id);
    }
    
    public Screen updateScreen(Long id,Screen screen) {
        Screen screenById = screenRepository.findById(id).orElse(null);
        if (screenById == null) {
            return null;
        }
        screenById.setLocation(screen.getLocation());
        screenById.setStatus(screen.getStatus());
        //screenById.setGroupID(screen.getGroupID());
        return screenRepository.save(screenById);
    }
    
    public Iterable<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    public List<Screen> getScreensByGroup(Long group) {
        return screenRepository.findByGroup(group);
    }

    
    
}
