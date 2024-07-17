package pt.ua.deti.uasmartsignage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.dto.TemplateDTO;
import pt.ua.deti.uasmartsignage.dto.TemplateWidgetDTO;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.models.Widget;
import pt.ua.deti.uasmartsignage.repositories.TemplateRepository;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final WidgetService widgetService;
    private final LogsService logsService;

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Template getTemplateById(String id) {
        return templateRepository.findById(id).orElse(null);
    }

    public Template saveTemplate(TemplateDTO templateDTO) {
        Template template = convertDTOToTemplate(templateDTO);
        if (template == null)
            return null;
        template.setId(null);
        return templateRepository.save(template);
    }

    public void deleteTemplateById(String id) {
        templateRepository.deleteById(id);
    }

    public Template updateTemplate(String id, TemplateDTO templateDTO) {
        Template template = convertDTOToTemplate(templateDTO);
        if (template == null)
            return null;
        template.setId(id);
        return templateRepository.save(template);
    }

    public Template convertDTOToTemplate(TemplateDTO templateDTO) {

        // create template
        Template template = Template.builder()
                .name(templateDTO.getName())
                .build();

        // add the widgets and default values to the template
        try {
            for (TemplateWidgetDTO dto : templateDTO.getWidgets()) {
                Widget widget = widgetService.getWidgetById(dto.getWidgetId());
                if (widget == null)
                    return null;
                template.addWidget(widget, dto.getTop(), dto.getLeft(), dto.getWidth(), dto.getHeight(), dto.getDefaultValues());
            }
        } catch (IllegalArgumentException exception) {
            logsService.addBackendLog(Severity.ERROR, 
                                        this.getClass().getSimpleName(),
                                        "convertDTOToTemplate", 
                                        exception.getMessage());
            return null;
        }

        return template;
    }
}
