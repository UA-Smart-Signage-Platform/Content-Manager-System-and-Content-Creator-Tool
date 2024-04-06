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

    public Monitor getMonitorById(Long id) {
        return monitorRepository.findById(id).orElse(null);
    }

    public Monitor saveMonitor(Monitor monitor) {
        return monitorRepository.save(monitor);
    }

    public void deleteMonitor(Long id) {
        monitorRepository.deleteById(id);
    }
    
    public Monitor updateMonitor(Long id, Monitor monitor) {
        Monitor monitorById = monitorRepository.findById(id).orElse(null);
        if (monitorById == null) {
            return null;
        }
        monitorById.setLocation(monitor.getLocation());
        monitorById.setMonitorsGroupForScreens(monitor.getMonitorsGroupForScreens());
        return monitorRepository.save(monitorById);
    }
    
    public Iterable<Monitor> getAllMonitors() {
        return monitorRepository.findAll();
    }

    public List<Monitor> getMonitorsByGroup(MonitorsGroup monitorsGroup) {
        return monitorRepository.findByMonitorsGroupForScreens(monitorsGroup);
    }

    
    
}
