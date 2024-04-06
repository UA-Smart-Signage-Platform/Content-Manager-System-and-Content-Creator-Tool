package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    public Template getTemplateById(Long id) {
        return templateRepository.findById(id).orElse(null);
    }

    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    public Template updateTemplate(Long id, Template template) {
        Template templateById = templateRepository.findById(id).orElse(null);
        if (templateById == null) {
            return null;
        }
        templateById.setName(template.getName());
        templateById.setPath(template.getPath());
        
        templateById.setTemplateWidgets(templateById.getTemplateWidgets());
        return templateRepository.save(templateById);
    }

    public Iterable<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Template getTemplateByName(String name) {
        return templateRepository.findByName(name);
    }


    
}
