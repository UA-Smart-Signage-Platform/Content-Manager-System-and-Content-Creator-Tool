package deti.uas.uasmartsignage.Controllers;

import org.springframework.stereotype.Controller;

import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.uas.uasmartsignage.Services.TemplateService;


@RestController
@RequestMapping("/templates")
public class TemplateController {

    
    private TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTemplates() {
        Iterable<Template> templates = templateService.getAllTemplates();
        return new ResponseEntity<>(templates, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveTemplate(@RequestBody Template template) {
        Template savedTemplate = templateService.saveTemplate(template);
        return new ResponseEntity<>(savedTemplate, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplateById(@PathVariable("id") Long id) {
        Template template = templateService.getTemplateById(id);
        if (template == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplate(@PathVariable("id") Long id, @RequestBody Template template) {
        Template updatedTemplate = templateService.updateTemplate(id, template);
        if (updatedTemplate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplate, HttpStatus.OK);
    }


    
}
