package pt.ua.deti.uasmartsignage.services;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.deti.uasmartsignage.models.Severity;
import pt.ua.deti.uasmartsignage.models.TemplateWidget;
import pt.ua.deti.uasmartsignage.Repositories.TemplateWidgetRepository;

@Service
public class TemplateWidgetService {

    private TemplateWidgetRepository templateWidgetRepository;
    
    private LogsService logsService;

    private final String source = this.getClass().getSimpleName();

    private static Logger logger = LoggerFactory.getLogger(TemplateWidgetService.class);

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";

    public TemplateWidgetService(TemplateWidgetRepository templateWidgetRepository, LogsService logsService) {
        this.templateWidgetRepository = templateWidgetRepository;
        this.logsService = logsService;
    }

    /**
     * Get a template widget by id
     * @param id The id of the template widget
     * @return The template widget
     */
    public TemplateWidget getTemplateWidgetById(Long id) {
        String operation = "getTemplateWidgetById";
        String description = "Getting template widget by id " + id;

        TemplateWidget templateWidget = templateWidgetRepository.findById(id).orElse(null);

        if (templateWidget == null) {
            return null;
        }

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateWidget;
    }

    /**
     * Save a template widget
     * @param templateWidget The template widget to save
     * @return The saved template widget
     */
    public TemplateWidget saveTemplateWidget(TemplateWidget templateWidget) {
        String operation = "saveTemplateWidget";
        String description = "Saving template widget " + templateWidget.getId();

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateWidgetRepository.save(templateWidget);
    }

    /**
     * Delete a template widget
     * @param id The id of the template widget to delete
     */
    public void deleteTemplateWidget(Long id) {

        String operation = "deleteTemplateWidget";
        String description = "Deleting template widget by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        templateWidgetRepository.deleteById(id);
    }

    /**
     * Update a template widget
     * @param id The id of the template widget to update
     * @param templateWidget The updated template widget
     * @return The updated template widget
     */
    public TemplateWidget updateTemplateWidget(Long id, TemplateWidget templateWidget) {
        TemplateWidget templateWidgetById = templateWidgetRepository.findById(id).orElse(null);
        if (templateWidgetById == null) {
            return null;
        }
        templateWidgetById.setTop(templateWidget.getTop());
        templateWidgetById.setLeftPosition(templateWidget.getLeftPosition());
        templateWidgetById.setWidth(templateWidget.getWidth());
        templateWidgetById.setHeight(templateWidget.getHeight());
        templateWidgetById.setWidget(templateWidget.getWidget());
        templateWidgetById.setTemplate(templateWidget.getTemplate());

        String operation = "updateTemplateWidget";
        String description = "Updating template widget " + templateWidgetById.getId();

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateWidgetRepository.save(templateWidgetById);
    }

    /**
     * Get all template widgets
     * @return All template widgets
     */
    public Iterable<TemplateWidget> getAllTemplateWidgets() {

        String operation = "getAllTemplateWidgets";
        String description = "Getting all template widgets";

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return templateWidgetRepository.findAll();
    }
    
}
