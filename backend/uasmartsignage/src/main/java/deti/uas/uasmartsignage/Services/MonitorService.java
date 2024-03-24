package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Models.Monitor;

import java.util.List;

@Service 
public class MonitorService {

    @Autowired
    private MonitorRepository monitorRepository;

    public Monitor getScreenById(Long id) {
        return monitorRepository.findById(id).orElse(null);
    }

    public Monitor saveScreen(Monitor monitor) {
        return monitorRepository.save(monitor);
    }

    public void deleteScreen(Long id) {
        monitorRepository.deleteById(id);
    }
    
    public Monitor updateScreen(Long id, Monitor monitor) {
        Monitor monitorById = monitorRepository.findById(id).orElse(null);
        if (monitorById == null) {
            return null;
        }
        monitorById.setLocation(monitor.getLocation());
        //screenById.setGroupID(screen.getGroupID());
        return monitorRepository.save(monitorById);
    }
    
    public Iterable<Monitor> getAllScreens() {
        return monitorRepository.findAll();
    }

    public List<Monitor> getScreensByGroup(MonitorsGroup monitorsGroup) {
        return monitorRepository.findByMonitorsGroupForScreens(monitorsGroup);
    }

    
    
}
