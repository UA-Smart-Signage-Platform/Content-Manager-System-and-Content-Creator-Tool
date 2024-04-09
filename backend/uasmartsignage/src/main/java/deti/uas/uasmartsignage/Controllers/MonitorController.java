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

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/monitors")
public class MonitorController {

    private MonitorService monitorService;

    @Operation(summary = "Get all monitors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all monitors", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<?> getAllMonitors() {
        List<Monitor> monitors = (List<Monitor>) monitorService.getAllMonitors();
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Get monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMonitorById(@PathVariable("id") Long id) {
        Monitor monitor = monitorService.getMonitorById(id);
        if (monitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitor, HttpStatus.OK);
    }

    @Operation(summary = "Get monitors by group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of monitors found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No monitors found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/group/{group}")
    public ResponseEntity<?> getMonitorsByGroup(@PathVariable("group") MonitorsGroup monitorsGroup) {
        List<Monitor> monitors = monitorService.getMonitorsByGroup(monitorsGroup);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Save monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Monitor saved", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Monitor already exists", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveMonitor(@RequestBody Monitor monitor) {
        Monitor savedMonitor = monitorService.saveMonitor(monitor);
        return new ResponseEntity<>(savedMonitor, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMonitor(@PathVariable("id") Long id, @RequestBody Monitor monitor) {
        Monitor updatedMonitor = monitorService.updateMonitor(id, monitor);
        if (updatedMonitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMonitor, HttpStatus.OK);
    }

    @Operation(summary = "Delete monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Monitor deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMonitor(@PathVariable("id") Long id) {
        monitorService.deleteMonitor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
