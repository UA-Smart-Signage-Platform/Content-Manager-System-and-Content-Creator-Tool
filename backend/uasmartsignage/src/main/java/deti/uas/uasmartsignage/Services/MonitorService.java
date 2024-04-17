package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Models.Monitor;


import java.util.List;

@Service 
public class MonitorService {

    private final MonitorRepository monitorRepository;

    @Autowired
    public MonitorService(MonitorRepository monitorRepository, MonitorGroupRepository monitorGroupRepository){
        this.monitorRepository = monitorRepository;
    }

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
        Monitor monitorById = monitorRepository.getReferenceById(id);
        if (monitorById == null) {
            return null;
        }
        monitorById.setName(monitor.getName());
        monitorById.setGroup(monitor.getGroup());
        return monitorRepository.save(monitorById);
    }

    public Monitor updatePending(Long id,boolean pending){
        Monitor monitorById = monitorRepository.getReferenceById(id);
        if (monitorById == null){
            return null;
        }
        monitorById.setPending(pending);
        return monitorRepository.save(monitorById);
    }
    
    public List<Monitor> getAllMonitorsByPending(boolean pending) {
        return monitorRepository.findByPending(pending);
    }

    public List<Monitor> getMonitorsByGroup(long groupId) {
        return monitorRepository.findByPendingAndGroup_Id(false,groupId);
    }

    public Monitor getMonitorByLocation(String location) {
        return monitorRepository.findByName(location);
    }

    
    
}
