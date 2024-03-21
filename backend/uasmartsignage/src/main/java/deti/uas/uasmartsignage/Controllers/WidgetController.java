package deti.uas.uasmartsignage.Controllers;


import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Services.ContentService;
import deti.uas.uasmartsignage.Services.WidgetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/widgets")
public class WidgetController {

    private WidgetService widgetService;

    private ContentService contentService;

    @GetMapping
    public ResponseEntity<?> getAllWidgets() {
        List<Widget> widgets = (List<Widget>) widgetService.getAllWidgets();
        return new ResponseEntity<>(widgets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWidgetById(@PathVariable("id") Long id) {
        Widget widget = widgetService.getWidgetById(id);
        if (widget == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(widget, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveWidget(@RequestBody Widget widget) {
        Long contentId = widget.getContent().getId();
        Content content = contentService.getContentById(contentId);
        widget.setContent(content);
        Widget savedWidget = widgetService.saveWidget(widget);
        return new ResponseEntity<>(savedWidget, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWidget(@PathVariable("id") Long id) {
        widgetService.deleteWidget(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
