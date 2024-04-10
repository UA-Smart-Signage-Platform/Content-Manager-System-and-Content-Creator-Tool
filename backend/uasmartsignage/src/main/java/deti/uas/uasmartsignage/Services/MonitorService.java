package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Services.MonitorGroupService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import deti.uas.uasmartsignage.Configuration.MqttConfig;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import deti.uas.uasmartsignage.Mqtt.GroupMessage;

import java.util.List;

@Service 
public class MonitorService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MonitorGroupService MonitorGroupService;

    public MonitorService(MonitorGroupService MonitorGroupService) {
        this.MonitorGroupService = MonitorGroupService;
    }

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
        MonitorsGroup monitorsGroup = MonitorGroupService.getGroupById(monitor.getMonitorsGroupForScreens().getId());
        
        if (monitorById.getMonitorsGroupForScreens() != monitor.getMonitorsGroupForScreens()) {
            try {
                GroupMessage confirmMessage = new GroupMessage();
                confirmMessage.setMethod("GROUP");
                confirmMessage.setGroup(monitorsGroup.getName());
    
                String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);
                System.out.println("Sending confirm message: " + confirmMessageJson);
    
                MqttConfig.getInstance().publish(monitor.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
            } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
                e.printStackTrace();
            }
        }

        monitorById.setMonitorsGroupForScreens(monitor.getMonitorsGroupForScreens());
        return monitorRepository.save(monitorById);
    }
    
    public Iterable<Monitor> getAllMonitors() {
        return monitorRepository.findAll();
    }

    public List<Monitor> getMonitorsByGroup(MonitorsGroup monitorsGroup) {
        return monitorRepository.findByMonitorsGroupForScreens(monitorsGroup);
    }

    public Monitor getMonitorByLocation(String location) {
        return monitorRepository.findByLocation(location);
    }

    
    
}
