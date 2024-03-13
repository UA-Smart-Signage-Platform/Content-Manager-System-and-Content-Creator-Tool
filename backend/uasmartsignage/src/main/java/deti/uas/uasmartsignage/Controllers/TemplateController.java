package deti.uas.uasmartsignage.Controllers;

import org.springframework.stereotype.Controller;

import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class TemplateController {

    @Autowired
    private TemplateRepository templateRepository;

    @GetMapping("/templates")
    public Iterable<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    @PostMapping("/templates")
    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }

    @GetMapping("/templates/{id}")
    public Template getTemplateById(Long id) {
        return templateRepository.findById(id).orElse(null);
    }
    
}
