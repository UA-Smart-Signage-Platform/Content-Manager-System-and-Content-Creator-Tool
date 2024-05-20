package deti.uas.uasmartsignage.Services;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import deti.uas.uasmartsignage.Repositories.WidgetRepository;
import deti.uas.uasmartsignage.Models.Severity;
import deti.uas.uasmartsignage.Models.Widget;

@Service
public class WidgetService {

    private WidgetRepository widgetRepository;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(WidgetService.class);

    private final String source = this.getClass().getSimpleName();

    private final LogsService logsService;

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";

    public WidgetService(WidgetRepository widgetRepository, LogsService logsService) {
        this.widgetRepository = widgetRepository;
        this.logsService = logsService;
    }

    /**
     * Retrieves and returns a Widget by its ID.
     *
     * @param id The ID of the Widget to retrieve.
     * @return The Widget with the specified ID.
     */
    public Widget getWidgetById(Long id) {
        String operation = "getWidgetById";
        String description = "Getting widget by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return widgetRepository.findById(id).orElse(null);
    }

    /**
     * Saves a Widget to the database.
     *
     * @param widget The Widget to save.
     * @return The saved Widget.
     */
    public Widget saveWidget(Widget widget) {
        String operation = "saveWidget";
        String description = "Saving widget " + widget.getName();

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return widgetRepository.save(widget);
    }

    /**
     * Deletes a Widget from the database.
     *
     * @param id The ID of the Widget to delete.
     */
    public void deleteWidget(Long id) {
        String operation = "deleteWidget";
        String description = "Deleting widget by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        widgetRepository.deleteById(id);
    }

    /**
     * Updates a Widget in the database.
     *
     * @param widget The Widget to update.
     * @return The updated Widget.
     */
    public Widget updateWidget(Long id, Widget widget) {
        String operation = "updateWidget";
        String description = "Updating widget by id " + id;

        Widget widgetById = widgetRepository.findById(id).orElse(null);
        if (widgetById == null) {
            return null;
        }
        widgetById.setName(widget.getName());
        widgetById.setPath(widget.getPath());
        widgetById.setContents(widget.getContents());

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return widgetRepository.save(widgetById);
    }

    /**
     * Retrieves and returns a list of all Widgets.
     *
     * @return A list of all Widgets.
     */
    public Iterable<Widget> getAllWidgets() {
        String operation = "getAllWidgets";
        String description = "Getting all widgets";

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return widgetRepository.findAll();
    }

    /**
     * Retrieves and returns a Widget by its name.
     *
     * @param name The name of the Widget to retrieve.
     * @return The Widget with the specified name.
     */
    public Widget getWidgetsByName(String name) {
        String operation = "getWidgetsByName";
        String description = "Getting widget by name " + name;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return widgetRepository.findByName(name);
    }
}
