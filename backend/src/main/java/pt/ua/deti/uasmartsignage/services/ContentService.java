package pt.ua.deti.uasmartsignage.services;

import org.springframework.stereotype.Service;

import pt.ua.deti.uasmartsignage.Repositories.ContentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pt.ua.deti.uasmartsignage.models.Content;
import pt.ua.deti.uasmartsignage.models.Severity;


@Service
public class ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepository contentRepository;

    private final LogsService logsService;

    private final String source = this.getClass().getSimpleName();

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";

    @Autowired
    public ContentService(ContentRepository contentRepository, LogsService logsService) {
        this.contentRepository = contentRepository;
        this.logsService = logsService;
    }

    /**
     * Get content by id
     * @param id The id of the content
     * @return The content
     */
    public Content getContentById(Long id) {
        String operation = "getContentById";
        String description = "Getting content by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }
        
        return contentRepository.findById(id).orElse(null);
    }

    /**
     * Save content
     * @param content The content to save
     * @return The saved content
     */
    public Content saveContent(Content content) {
        String operation = "saveContent";
        String description = "Saving content: " + content.getName();

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return contentRepository.save(content);
    }

    /**
     * Delete content by id
     * @param id The id of the content
     */
    public void deleteContent(Long id) {

        String operation = "deleteContent";
        String description = "Deleting content by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        contentRepository.deleteById(id);
    }

    /**
     * Update content by id
     * @param id The id of the content
     * @param content The content to update
     * @return The updated content
     */
    public Content updateContent(Long id, Content content) {
        Content contentById = contentRepository.findById(id).orElse(null);
        if (contentById == null) {
            return null;
        }
        contentById.setName(content.getName());
        contentById.setType(content.getType());
        contentById.setOptions(content.getOptions());

        String operation = "updateContent";
        String description = "Updating content by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return contentRepository.save(contentById);
    }

    /**
     * Get all contents
     * @return All contents
     */
    public Iterable<Content> getAllContents() {
        String operation = "getAllContents";
        String description = "Getting all contents";

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return contentRepository.findAll();
    }
}
