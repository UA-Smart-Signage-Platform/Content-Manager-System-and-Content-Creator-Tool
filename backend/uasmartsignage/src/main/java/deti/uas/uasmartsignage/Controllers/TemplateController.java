package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.Template;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import deti.uas.uasmartsignage.Services.TemplateService;


@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    
    private TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Operation(summary = "Get all templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all templates", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No templates found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllTemplates() {
        Iterable<Template> templates = templateService.getAllTemplates();
        return new ResponseEntity<>(templates, HttpStatus.OK);
    }

    @Operation(summary = "Save template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template saved", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveTemplate(@RequestBody Template template) {
        Template savedTemplate = templateService.saveTemplate(template);
        return new ResponseEntity<>(savedTemplate, HttpStatus.CREATED);
    }

    @Operation(summary = "Get template by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplateById(@PathVariable("id") Long id) {
        Template template = templateService.getTemplateById(id);
        if (template == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @Operation(summary = "Update template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplate(@PathVariable("id") Long id, @RequestBody Template template) {
        Template updatedTemplate = templateService.updateTemplate(id, template);
        if (updatedTemplate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplate, HttpStatus.OK);
    }

    @Operation(summary = "Delete template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Template deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable("id") Long id) {
        templateService.deleteTemplate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    
}
