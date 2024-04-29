package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Models.Monitor;


import java.util.List;

@Service 
public class MonitorService {

    private final MonitorRepository monitorRepository;

    private final MonitorGroupRepository monitorGroupRepository;


    public MonitorService(MonitorRepository monitorRepository, MonitorGroupRepository monitorGroupRepository){
        this.monitorRepository = monitorRepository;
        this.monitorGroupRepository = monitorGroupRepository;
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
        MonitorsGroup group = monitorById.getGroup();
        monitorById.setName(monitor.getName());
        monitorById.setGroup(monitor.getGroup());
        Monitor returnMonitor = monitorRepository.save(monitorById);
        if (group.isMadeForMonitor() && group.getId() == monitor.getGroup().getId()) {
            monitorById.getGroup().setName(monitor.getName());
        }
        if (group.isMadeForMonitor() && group.getId() != monitor.getGroup().getId()) {
            monitorGroupRepository.deleteById(group.getId());
        }
        return returnMonitor;
    }

    public Monitor updatePending(Long id,boolean pending){
        Monitor monitorById = monitorRepository.getReferenceById(id);
        monitorById.setPending(pending);
        return monitorRepository.save(monitorById);
    }
    
    public List<Monitor> getAllMonitorsByPending(boolean pending) {
        return monitorRepository.findByPending(pending);
    }

    public List<Monitor> getMonitorsByGroup(long groupId) {
        return monitorRepository.findByPendingAndGroup_Id(false,groupId);
    }

}
