package pt.ua.deti.uasmartsignage.controllers;

import pt.ua.deti.uasmartsignage.models.Widget;
import pt.ua.deti.uasmartsignage.services.WidgetService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // NOSONAR
@RequiredArgsConstructor
@RequestMapping("/api/widgets")
public class WidgetController {

    private final WidgetService widgetService;

    @Operation(summary = "Get all widgets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all widgets")
    })
    @GetMapping
    public ResponseEntity<List<Widget>> getAllWidgets() {
        List<Widget> widgets = widgetService.getAllWidgets();
        return new ResponseEntity<>(widgets, HttpStatus.OK);
    }

    @Operation(summary = "Get widget by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved widget"),
            @ApiResponse(responseCode = "404", description = "Widget not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable("id") String id) {
        Widget widget = widgetService.getWidgetById(id);
        if (widget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(widget, HttpStatus.OK);
    }
}
