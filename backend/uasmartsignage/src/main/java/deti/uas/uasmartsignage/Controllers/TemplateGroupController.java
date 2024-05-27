package deti.uas.uasmartsignage.Controllers;

import java.util.List;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.uas.uasmartsignage.Services.MonitorGroupService;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import deti.uas.uasmartsignage.Services.TemplateService;

import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/templateGroups")
public class TemplateGroupController {

    private TemplateGroupService templateGroupService;
    private MonitorGroupService monitorGroupService;
    private TemplateService templateService;

    @Operation(summary = "Get all template groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all template groups", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<List<TemplateGroup>> getAllTemplateGroups() {
        List<TemplateGroup> templateGroups = templateGroupService.getAllGroups();
        return new ResponseEntity<>(templateGroups, HttpStatus.OK);
    }

    @Operation(summary = "Get template group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template group found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TemplateGroup> getTemplateGroupById(@PathVariable("id") Long id) {
        TemplateGroup templateGroup = templateGroupService.getGroupById(id);
        if (templateGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(templateGroup, HttpStatus.OK);
    }

    @Operation(summary = "Save template group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template group saved", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<TemplateGroup> saveTemplateGroup(@RequestBody TemplateGroup templateGroup) {
        TemplateGroup savedTemplateGroup = templateGroupService.saveGroup(templateGroup);
        return new ResponseEntity<>(savedTemplateGroup, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete template group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Template group deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplateGroup(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        templateGroupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete a list of template groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Template groups deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template groups not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTemplateGroup(@RequestBody List<Integer> templateGroupsIds) {
        if (templateGroupsIds == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        templateGroupService.deleteGroups(templateGroupsIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    

    @Operation(summary = "Update template group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template group updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template group not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TemplateGroup> updateTemplateGroup(@PathVariable("id") Long id, @RequestBody TemplateGroup templateGroup) {
        TemplateGroup updatedTemplateGroup = templateGroupService.updateTemplateGroup(id, templateGroup);
        if (updatedTemplateGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplateGroup, HttpStatus.OK);
    }
    
}
