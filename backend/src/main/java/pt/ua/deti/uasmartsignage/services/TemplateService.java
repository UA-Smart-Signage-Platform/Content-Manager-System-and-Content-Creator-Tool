package pt.ua.deti.uasmartsignage.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.dto.TemplateDTO;
import pt.ua.deti.uasmartsignage.dto.TemplateWidgetDTO;
import pt.ua.deti.uasmartsignage.enums.Log;
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

    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);
    private final String source = this.getClass().getSimpleName();

    /**
     * Retrieves all existent templates.
     *
     * @return All templates.
    */
    public List<Template> getAllTemplates() {
        String operation = "getAllTemplates";
        String description = "Retrieved all templates";

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        return templateRepository.findAll();
    }

    /**
     * Retrieves template based on given ID.
     *
     * @param id The ID of the template.
     * @return The template based on the ID or empty if not found.
    */
    public Optional<Template> getTemplateById(String id) {
        logger.info("Retrieving template with ID: {}", id);
        Optional<Template> template = templateRepository.findById(id);

        String operation = "getTemplateById";
        String description = "Retrieved template with ID: " + id;

        if (template.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return Optional.empty();
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return template;
    }

    /**
     * Saves template based on Data Transfer Object (DTO).
     * 
     * @param templateDTO The DTO with information necessary to create a template.
     * @return The newly created Template, or null if no template was created.
    */
    public Template saveTemplate(TemplateDTO templateDTO) {
        Template template = convertDTOToTemplate(templateDTO);

        String operation = "saveTemplate(DTO)";
        String description = "Saved template with name: " + templateDTO.getName();

        if (template == null){
            description = "Failed to create template with name: " + templateDTO.getName();
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        } 

        template.setId(null);
        Template savedTemplate = templateRepository.save(template);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return savedTemplate;
    }

    /**
     * Deletes template based on given ID.
     * 
     * @param id The ID of the desired template to delete.
     * @return Boolean with true if template was deleted or false if it failed.
    */
    public boolean deleteTemplateById(String id) {
        Optional<Template> template = getTemplateById(id);

        String operation = "deleteTemplateById";
        String description = "Deleted template with ID: " + id;

        if (template.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return false;
        }

        templateRepository.deleteById(id);
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return true;
    }

    /**
     * Updates template based on ID and Data Transfer Object (DTO).
     * 
     * @param id The ID of the desired rule to template.
     * @param templateDTO The DTO with information necessary to update the template.
     * @return The updated template.
    */
    public Template updateTemplate(String id, TemplateDTO templateDTO) {
        Template template = convertDTOToTemplate(templateDTO);

        String operation = "updateTemplate(DTO)";
        String description = "Updated template with ID: " + id + "; with name: " + templateDTO.getName();

        if (template == null){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        } 

        template.setId(id);
        Template updatedTemplate = templateRepository.save(template);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return updatedTemplate;
    }

    public Template convertDTOToTemplate(TemplateDTO templateDTO) {
        String operation = "convertDTOToTemplate";
        String description = "Converted templateDTO to Template with name: " + templateDTO.getName();

        // create template
        Template template = Template.builder()
                .name(templateDTO.getName())
                .build();

        // add the widgets and default values to the template
        try {
            for (TemplateWidgetDTO dto : templateDTO.getWidgets()) {
                Widget widget = widgetService.getWidgetById(dto.getWidgetId());
                if (widget == null){
                    description = "TemplateDTO has problems with widgets. Widget found was NULL; templateDTO name: " + templateDTO.getName();
                    logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
                    return null;
                }
                template.addWidget(widget, dto.getTop(), dto.getLeft(), dto.getWidth(), dto.getHeight(), dto.getZindex(), dto.getDefaultValues());
            }
        } catch (IllegalArgumentException exception) {
            description = "Something went wrong when adding widgets and default values to Template; exception: " + exception.getMessage();
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        }

        return template;
    }
}
