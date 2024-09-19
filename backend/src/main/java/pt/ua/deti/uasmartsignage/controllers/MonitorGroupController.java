package pt.ua.deti.uasmartsignage.controllers;

import java.util.List;
import java.util.Optional;

import pt.ua.deti.uasmartsignage.dto.MonitorGroupDTO;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.uasmartsignage.services.MonitorGroupService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*") // NOSONAR
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class MonitorGroupController {

    private final MonitorGroupService monitorGroupService;

    @Operation(summary = "Get all monitor groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all monitor groups")
    })
    @GetMapping
    public ResponseEntity<List<MonitorGroup>> getAllGroups() {
        List<MonitorGroup> groups = monitorGroupService.getAllGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @Operation(summary = "Get all the monitor groups, ignoring the ones that are created for only a specific monitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all non default monitor groups")
    })
    @GetMapping("/ignoreDefault")
    public ResponseEntity<List<MonitorGroup>> getAllNonDefaultGroups() {
        List<MonitorGroup> groups = monitorGroupService.getAllNonDefaultGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @Operation(summary = "Get monitor group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved monitor group"),
            @ApiResponse(responseCode = "404", description = "Monitor group not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<MonitorGroup>> getGroupById(@PathVariable("id") Long id) {
        Optional<MonitorGroup> group = monitorGroupService.getGroupById(id);
        if (group.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Operation(summary = "Save monitor group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully saved monitor group"),
            @ApiResponse(responseCode = "409", description = "A group with the given name already exists"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<MonitorGroup> saveGroup(@RequestBody @Valid MonitorGroupDTO groupDTO) {
        Optional<MonitorGroup> existingGroup = monitorGroupService.getGroupByName(groupDTO.getName());
        if (existingGroup.isPresent()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        MonitorGroup savedGroup = monitorGroupService.saveGroup(groupDTO);
        if (savedGroup == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete monitor group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted monitor group"),
            @ApiResponse(responseCode = "404", description = "Monitor group not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGroup(@PathVariable("id") Long id) {
        Boolean deleted = monitorGroupService.deleteGroupById(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update monitor group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated monitor group"),
            @ApiResponse(responseCode = "404", description = "Monitor group not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MonitorGroup> updateGroup(@PathVariable("id") Long id, @RequestBody @Valid MonitorGroupDTO groupDTO) {
        MonitorGroup updatedGroup = monitorGroupService.updateGroup(id, groupDTO);
        if (updatedGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }
    
}
