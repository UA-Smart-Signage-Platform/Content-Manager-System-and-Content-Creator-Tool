package pt.ua.deti.uasmartsignage.controllers;

import java.util.List;

import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorsGroup;
import pt.ua.deti.uasmartsignage.models.TemplateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.uasmartsignage.services.MonitorGroupService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*") //NOSONAR
@RequestMapping("/api/groups")
public class MonitorGroupController {

    private MonitorGroupService monitorGroupService;

    @Operation(summary = "Get all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all groups", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<List<MonitorsGroup>> getAllGroups() {
        List<MonitorsGroup> monitorsGroups =  monitorGroupService.getAllGroups();
        return new ResponseEntity<>(monitorsGroups, HttpStatus.OK);
    }

    @Operation(summary = "Get all groups without the ones made for unique monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all groups", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping("/notMadeForMonitor")
    public ResponseEntity<List<MonitorsGroup>> getAllGroupsNotMadeForMonitor() {
        List<MonitorsGroup> monitorsGroups = monitorGroupService.getAllGroupsNotMadeForMonitor();
        return new ResponseEntity<>(monitorsGroups, HttpStatus.OK);
    }

    @Operation(summary = "Get group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MonitorsGroup> getGroupById(@PathVariable("id") Long id) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup, HttpStatus.OK);
    }

    @Operation(summary = "Get group by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<MonitorsGroup> getGroupByName(@PathVariable("name") String name) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupByName(name);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup, HttpStatus.OK);
    }

    @Operation(summary = "Get all monitors from group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all groups found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No groups found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}/monitors")
    public ResponseEntity<List<Monitor>> getMonitorsByGroup(@PathVariable("id") Long id){
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Monitor> monitors = monitorsGroup.getMonitors();
        return new ResponseEntity<>(monitors, HttpStatus.OK);
    }

    @Operation(summary = "Get template from group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}/template")
    public ResponseEntity<List<TemplateGroup>> getTemplateByGroup(@PathVariable("id") Long id) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup.getTemplateGroups(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<MonitorsGroup> createGroup(@RequestBody MonitorsGroup monitorsGroup) {
        MonitorsGroup newMonitorsGroup = monitorGroupService.saveGroup(monitorsGroup);
        if (newMonitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newMonitorsGroup, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<MonitorsGroup> updateGroup(@PathVariable("id") Long id, @RequestBody MonitorsGroup monitorsGroup) {
        MonitorsGroup updatedMonitorsGroup = monitorGroupService.updateGroup(id, monitorsGroup);
        if (updatedMonitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMonitorsGroup, HttpStatus.OK);
    }

    @Operation(summary = "Delete a group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        monitorGroupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}