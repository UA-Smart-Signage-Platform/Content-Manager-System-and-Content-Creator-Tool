package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Services.MonitorGroupService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import deti.uas.uasmartsignage.Configuration.MqttConfig;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.util.List;

@Service 
public class MonitorService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MonitorGroupService MonitorGroupService;

    

    private final MonitorRepository monitorRepository;

    @Autowired
    public MonitorService(MonitorRepository monitorRepository, MonitorGroupService monitorGroupService){
        this.monitorRepository = monitorRepository;
        this.MonitorGroupService = monitorGroupService;
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
