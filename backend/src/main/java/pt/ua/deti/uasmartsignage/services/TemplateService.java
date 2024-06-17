package pt.ua.deti.uasmartsignage.services;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import pt.ua.deti.uasmartsignage.models.Severity;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.models.TemplateWidget;
import pt.ua.deti.uasmartsignage.repositories.TemplateRepository;

@Service
public class TemplateService {

    private TemplateRepository templateRepository;

    private final LogsService logsService;

    private final String source = this.getClass().getSimpleName();

    private Logger logger = org.slf4j.LoggerFactory.getLogger(TemplateService.class);

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";

    public TemplateService(TemplateRepository templateRepository, LogsService logsService) {
        this.templateRepository = templateRepository;
        this.logsService = logsService;
    }


    /**
     * Retrieves and returns a Template by its ID.
     *
     * @param id The ID of the Template to retrieve.
     * @return The Template with the specified ID.
     */
    public Template getTemplateById(Long id) {
        String operation = "getTemplateById";
        String description = "Getting template by id " + id;

        Template template = templateRepository.findById(id).orElse(null);

        if (template == null) {
            return null;
        }

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return template;
    }

    /**
     * Saves a Template to the database.
     *
     * @param template The Template to save.
     * @return The saved Template.
     */
    public Template saveTemplate(Template template) {
        String operation = "saveTemplate";
        String description = "Saving template " + template.getName();

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        for ( TemplateWidget templateWidget : template.getTemplateWidgets()) {
            templateWidget.setTemplate(template);
        }
        
        return templateRepository.save(template);
    }

    /**
     * Deletes a Template from the database.
     *
     * @param id The ID of the Template to delete.
     */
    public void deleteTemplate(Long id) {
        String operation = "deleteTemplate";
        String description = "Deleting template by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        templateRepository.deleteById(id);
    }

    /**
     * Updates a Template in the database.
     *
     * @param id The ID of the Template to update.
     * @param template The Template to update.
     * @return The updated Template.
     */
    public Template updateTemplate(Long id, Template template) {
        String operation = "updateTemplate";
        String description = "Updating template by id " + id;

        Template templateById = templateRepository.findById(id).orElse(null);
        if (templateById == null) {
            return null;
        }
        templateById.setName(template.getName());
        
        templateById.setTemplateWidgets(templateById.getTemplateWidgets());

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateRepository.save(templateById);
    }

    /**
     * Retrieves and returns all Templates.
     *
     * @return All Templates.
     */
    public Iterable<Template> getAllTemplates() {
        String operation = "getAllTemplates";
        String description = "Getting all templates";

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateRepository.findAll();
    }

    /**
     * Retrieves and returns a Template by its name.
     *
     * @param name The name of the Template to retrieve.
     * @return The Template with the specified name.
     */
    public Template getTemplateByName(String name) {
        String operation = "getTemplateByName";
        String description = "Getting template by name " + name;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateRepository.findByName(name);
    }


    
}
