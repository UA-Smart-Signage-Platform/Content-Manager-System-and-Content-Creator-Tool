package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Services.TemplateWidgetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/templateWidgets")
public class TemplateWidgetController {

    private TemplateWidgetService templateWidgetService;

    @GetMapping
    public ResponseEntity<?> getAllTemplateWidgets() {
        List<TemplateWidget> templateWidgets = (List<TemplateWidget>) templateWidgetService.getAllTemplateWidgets();
        return new ResponseEntity<>(templateWidgets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplateWidgetById(@PathVariable("id") Long id) {
        TemplateWidget templateWidget = templateWidgetService.getTemplateWidgetById(id);
        if (templateWidget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(templateWidget, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveTemplateWidget(@RequestBody TemplateWidget templateWidget) {
        TemplateWidget savedTemplateWidget = templateWidgetService.saveTemplateWidget(templateWidget);
        return new ResponseEntity<>(savedTemplateWidget, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplateWidget(@PathVariable("id") Long id) {
        templateWidgetService.deleteTemplateWidget(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplateWidget(@PathVariable("id") Long id, @RequestBody TemplateWidget templateWidget) {
        TemplateWidget updatedTemplateWidget = templateWidgetService.updateTemplateWidget(id, templateWidget);
        if (updatedTemplateWidget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplateWidget, HttpStatus.OK);
    }
    
}
