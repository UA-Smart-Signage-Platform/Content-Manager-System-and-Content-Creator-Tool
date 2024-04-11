package deti.uas.uasmartsignage.Controllers;

import java.util.List;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.uas.uasmartsignage.Services.MonitorGroupService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class MonitorGroupController {

    private MonitorGroupService monitorGroupService;

    @Operation(summary = "Get all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all groups", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        List<MonitorsGroup> monitorsGroups = (List<MonitorsGroup>) monitorGroupService.getAllGroups();
        return new ResponseEntity<>(monitorsGroups, HttpStatus.OK);
    }

    @Operation(summary = "Get group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable("id") Long id) {
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
    public ResponseEntity<?> getGroupByName(@PathVariable("name") String name) {
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
    @GetMapping("/{id}/screens")
    public ResponseEntity<?> getScreensByGroup(@PathVariable("id") Long id){
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup.getMonitors(), HttpStatus.OK);
    }

    @Operation(summary = "Get template from group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}/template")
    public ResponseEntity<?> getTemplateByGroup(@PathVariable("id") Long id) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup.getTemplateGroup(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody MonitorsGroup monitorsGroup) {
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
    public ResponseEntity<?> updateGroup(@PathVariable("id") Long id, @RequestBody MonitorsGroup monitorsGroup) {
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
