package deti.uas.uasmartsignage.Controllers;


import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Services.ContentService;
import deti.uas.uasmartsignage.Services.WidgetService;
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
@RequestMapping("/widgets")
public class WidgetController {

    private WidgetService widgetService;

    private ContentService contentService;

    @Operation(summary = "Get all widgets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all widgets", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<?> getAllWidgets() {
        List<Widget> widgets = (List<Widget>) widgetService.getAllWidgets();
        return new ResponseEntity<>(widgets, HttpStatus.OK);
    }

    @Operation(summary = "Get widget by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Widget found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Widget not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getWidgetById(@PathVariable("id") Long id) {
        Widget widget = widgetService.getWidgetById(id);
        if (widget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(widget, HttpStatus.OK);
    }

    @Operation(summary = "Save widget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Widget saved", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> saveWidget(@RequestBody Widget widget) {
        // Long contentId = widget.getContent().getId();
        // Content content = contentService.getContentById(contentId);
        // widget.setContent(content);
        Widget savedWidget = widgetService.saveWidget(widget);
        return new ResponseEntity<>(savedWidget, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete widget by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Widget deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Widget not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWidget(@PathVariable("id") Long id) {
        widgetService.deleteWidget(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update widget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Widget updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Widget not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWidget(@PathVariable("id") Long id, @RequestBody Widget widget) {
        Widget updatedWidget = widgetService.updateWidget(id, widget);
        if (updatedWidget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedWidget, HttpStatus.OK);
    }





}
