package pt.ua.deti.uasmartsignage.controllers;

import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.services.MonitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;


import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") //NOSONAR
@RestController
@RequestMapping("/api/monitors")
public class MonitorController {

    private MonitorService monitorService;

    @Autowired
    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Operation(summary = "Get all non Pending monitors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all monitors that are not pending. List can be filtered by online status.", content = @Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<List<Monitor>> getAllMonitors(@RequestParam(required = false) Boolean onlineStatus) {
        List<Monitor> monitors;

        if (onlineStatus != null){
            monitors = monitorService.getAllMonitorsByPendingAndOnline(false, onlineStatus);
        }
        else {
            monitors = monitorService.getAllMonitorsByPending(false);
        }

        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Get all Pending monitors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all pending monitors", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/pending")
    public ResponseEntity<List<Monitor>> getPendingMonitors(){
        List<Monitor> monitors = monitorService.getAllMonitorsByPending(true);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Accept one pending Monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor accepted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/accept/{id}")
    public ResponseEntity<Monitor> acceptPendingMonitor(@PathVariable("id") Long id){
        Monitor monitor = monitorService.updatePending(id, false);
        if(monitor == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitor,HttpStatus.OK);
    }

    @Operation(summary = "Get monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Monitor>> getMonitorById(@PathVariable("id") Long id) {
        Optional<Monitor> monitor = monitorService.getMonitorById(id);
        if (monitor.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitor, HttpStatus.OK);
    }

    @Operation(summary = "Get monitors by group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of monitors found. List can be filtered by online status.", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/group/{group}")
    public ResponseEntity<List<Monitor>> getMonitorsByGroup(@PathVariable("group") Long group, @RequestParam(value = "onlineStatus", required = false) Boolean onlineStatus) {
        List<Monitor> monitors;

        if (onlineStatus != null){
            monitors = monitorService.getMonitorsByGroupAndOnline(group, onlineStatus);
        }
        else {
            monitors = monitorService.getMonitorsByGroup(group);
        }
        
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Save monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Monitor saved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Monitor already exists", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Monitor> saveMonitor(@RequestBody Monitor monitor) {
        Monitor savedMonitor = monitorService.saveMonitor(monitor);
        return new ResponseEntity<>(savedMonitor, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Monitor> updateMonitor(@PathVariable("id") Long id, @RequestBody Monitor monitor) {
        Monitor updatedMonitor = monitorService.updateMonitor(id, monitor);
        if (updatedMonitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMonitor, HttpStatus.OK);
    }

    @Operation(summary = "Delete monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Monitor deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMonitor(@PathVariable("id") Long id) {
        boolean deleted = monitorService.deleteMonitor(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
    
}
