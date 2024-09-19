package pt.ua.deti.uasmartsignage.controllers;

import pt.ua.deti.uasmartsignage.dto.TemplateDTO;
import pt.ua.deti.uasmartsignage.models.Template;

import java.util.List;
import java.util.Optional;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.services.TemplateService;

@RestController
@CrossOrigin(origins = "*") // NOSONAR
@RequiredArgsConstructor
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "Get all templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all templates")
    })
    @GetMapping
    public ResponseEntity<List<Template>> getAllTemplates() {
        List<Template> templates = templateService.getAllTemplates();
        return new ResponseEntity<>(templates, HttpStatus.OK);
    }

    @Operation(summary = "Get template by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved template"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Template>> getTemplateById(@PathVariable("id") String id) {
        Optional<Template> template = templateService.getTemplateById(id.replaceAll("[\n\r]", "_"));
        if (template.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @Operation(summary = "Save template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully saved template"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Template> saveTemplate(@RequestBody @Valid TemplateDTO templateDTO) {
        Template savedTemplate = templateService.saveTemplate(templateDTO);
        if (savedTemplate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedTemplate, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted template"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTemplate(@PathVariable("id") String id) {
        boolean deleted = templateService.deleteTemplateById(id.replaceAll("[\n\r]", "_"));
        if (!deleted) {
            return new ResponseEntity<>(deleted, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated template"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Template> updateTemplate(@PathVariable("id") String id, @RequestBody @Valid TemplateDTO templateDTO) {
        Template updatedTemplate = templateService.updateTemplate(id, templateDTO);
        if (updatedTemplate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplate, HttpStatus.OK);
    }
}
