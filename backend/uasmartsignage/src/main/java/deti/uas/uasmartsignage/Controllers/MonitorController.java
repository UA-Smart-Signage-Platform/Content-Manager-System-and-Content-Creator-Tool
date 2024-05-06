package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Services.MonitorService;
import deti.uas.uasmartsignage.authentication.IAuthenticationFacade;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/monitors")
public class MonitorController {

    private MonitorService monitorService;

    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public MonitorController(MonitorService monitorService, IAuthenticationFacade authenticationFacade) {
        this.monitorService = monitorService;
        this.authenticationFacade = authenticationFacade;
    }

    @Operation(summary = "Get all monitors not Pending")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all monitors that are not pending", content = @Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<?> getAllMonitors() {
        
        List<Monitor> monitors = monitorService.getAllMonitorsByPending(false);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Get all pendign monitors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all pendign monitors", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingMonitors(){
        List<Monitor> monitors = monitorService.getAllMonitorsByPending(true);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Accept one pending Monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor accepted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptPendingMonitor(@PathVariable("id") Long id){
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
    public ResponseEntity<?> getMonitorById(@PathVariable("id") Long id) {
        Monitor monitor = monitorService.getMonitorById(id);
        if (monitor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitor, HttpStatus.OK);
    }

    @Operation(summary = "Get monitors by group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of monitors found", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/group/{group}")
    public ResponseEntity<?> getMonitorsByGroup(@PathVariable("group") Long group) {
        List<Monitor> monitors = monitorService.getMonitorsByGroup(group);
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Save monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Monitor saved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Monitor already exists", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveMonitor(@RequestBody Monitor monitor) {
        Monitor savedMonitor = monitorService.saveMonitor(monitor);
        return new ResponseEntity<>(savedMonitor, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit monitor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitor updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
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
            @ApiResponse(responseCode = "204", description = "Monitor deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitor not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMonitor(@PathVariable("id") Long id) {
        monitorService.deleteMonitor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
