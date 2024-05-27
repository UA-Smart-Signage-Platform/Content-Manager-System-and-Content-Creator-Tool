package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Services.TemplateWidgetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/templateWidgets")
@CrossOrigin(origins = "*")
public class TemplateWidgetController {

    private TemplateWidgetService templateWidgetService;

    @Operation(summary = "Get all template widgets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all template widgets", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<List<TemplateWidget>> getAllTemplateWidgets() {
        List<TemplateWidget> templateWidgets = (List<TemplateWidget>) templateWidgetService.getAllTemplateWidgets();
        return new ResponseEntity<>(templateWidgets, HttpStatus.OK);
    }

    @Operation(summary = "Get template widget by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template widget found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template widget not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TemplateWidget> getTemplateWidgetById(@PathVariable("id") Long id) {
        TemplateWidget templateWidget = templateWidgetService.getTemplateWidgetById(id);
        if (templateWidget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(templateWidget, HttpStatus.OK);
    }

    @Operation(summary = "Save template widget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template widget saved", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<TemplateWidget> saveTemplateWidget(@RequestBody TemplateWidget templateWidget) {
        TemplateWidget savedTemplateWidget = templateWidgetService.saveTemplateWidget(templateWidget);
        return new ResponseEntity<>(savedTemplateWidget, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete template widget by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Template widget deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template widget not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplateWidget(@PathVariable("id") Long id) {
        templateWidgetService.deleteTemplateWidget(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update template widget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template widget updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Template widget not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TemplateWidget> updateTemplateWidget(@PathVariable("id") Long id, @RequestBody TemplateWidget templateWidget) {
        TemplateWidget updatedTemplateWidget = templateWidgetService.updateTemplateWidget(id, templateWidget);
        if (updatedTemplateWidget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplateWidget, HttpStatus.OK);
    }
    
}
