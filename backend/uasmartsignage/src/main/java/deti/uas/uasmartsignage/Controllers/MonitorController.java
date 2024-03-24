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
    public ResponseEntity<?> getAllScreens() {
        List<Monitor> monitors = (List<Monitor>) monitorService.getAllScreens();
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScreenById(@PathVariable("id") Long id) {
        Monitor monitor = monitorService.getScreenById(id);
        if (monitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitor, HttpStatus.OK);
    }

    @GetMapping("/group/{group}")
    public ResponseEntity<?> getScreensByGroup(@PathVariable("group") MonitorsGroup monitorsGroup) {
        List<Monitor> monitors = monitorService.getScreensByGroup(monitorsGroup);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveScreen(@RequestBody Monitor monitor) {
        Monitor savedMonitor = monitorService.saveScreen(monitor);
        return new ResponseEntity<>(savedMonitor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScreen(@PathVariable("id") Long id, @RequestBody Monitor monitor) {
        Monitor updatedMonitor = monitorService.updateScreen(id, monitor);
        if (updatedMonitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMonitor, HttpStatus.OK);
    }
    
}
