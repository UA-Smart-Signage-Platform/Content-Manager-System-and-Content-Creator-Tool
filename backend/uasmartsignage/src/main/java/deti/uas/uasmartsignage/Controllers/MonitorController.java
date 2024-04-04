package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Mqtt.MqttPublish;
import deti.uas.uasmartsignage.Configuration.MqttConfig;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Services.MonitorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/monitors")
public class MonitorController {

    private MonitorService monitorService;

    @GetMapping
    public ResponseEntity<?> getAllMonitors() {
        List<Monitor> monitors = (List<Monitor>) monitorService.getAllMonitors();
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMonitorById(@PathVariable("id") Long id) {
        Monitor monitor = monitorService.getMonitorById(id);
        if (monitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitor, HttpStatus.OK);
    }

    @GetMapping("/group/{group}")
    public ResponseEntity<?> getMonitorsByGroup(@PathVariable("group") MonitorsGroup monitorsGroup) {
        List<Monitor> monitors = monitorService.getMonitorsByGroup(monitorsGroup);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveMonitor(@RequestBody Monitor monitor) {
        Monitor savedMonitor = monitorService.saveMonitor(monitor);
        return new ResponseEntity<>(savedMonitor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMonitor(@PathVariable("id") Long id, @RequestBody Monitor monitor) {
        Monitor updatedMonitor = monitorService.updateMonitor(id, monitor);
        if (updatedMonitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMonitor, HttpStatus.OK);
    }
    
}
