package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorsGroup;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MonitorGroupService {

    private final MonitorGroupRepository monitorGroupRepository;

    private final MonitorRepository monitorRepository;

    private final LogsService logsService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MonitorGroupService.class);

    private final String source = this.getClass().getSimpleName();

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";

    public MonitorGroupService(MonitorGroupRepository monitorGroupRepository, MonitorRepository monitorRepository, LogsService logsService){
        this.monitorGroupRepository = monitorGroupRepository;
        this.monitorRepository = monitorRepository;
        this.logsService = logsService;
    }

    /**
     * Retrieves and returns a MonitorsGroup by its ID.
     *
     * @param id The ID of the MonitorsGroup to retrieve.
     * @return The MonitorsGroup with the specified ID.
     */
    public MonitorsGroup getGroupById(Long id) {
        String operation = "getGroupById";
        String description = "Getting group by id " + id;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return monitorGroupRepository.findById(id).orElse(null);
    }

    /**
     * Saves a MonitorsGroup to the database.
     *
     * @param monitorsGroup The MonitorsGroup to save.
     * @return The saved MonitorsGroup.
     */
    public MonitorsGroup saveGroup(MonitorsGroup monitorsGroup) {
        String operation = "saveGroup";
        String description = "Saving group " + monitorsGroup.getName();

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return monitorGroupRepository.save(monitorsGroup);
    }

    /**
     * Deletes a MonitorsGroup from the database and creates new groups for all the monitors that the group had.
     *
     * @param id The ID of the MonitorsGroup to delete.
     */
    @Transactional
    public void deleteGroup(Long id) {
        String operation = "deleteGroup";
        String description = "Deleting group by id " + id;

        Optional<MonitorsGroup> group = monitorGroupRepository.findById(id);
        if (group.isPresent()) {
            if (group.get().getMonitors().isEmpty()) {
                monitorGroupRepository.deleteById(id);
                return;
            }
            for (int i = 0; i < group.get().getMonitors().size(); i++) {
                MonitorsGroup monitorsGroup = new MonitorsGroup();
                Monitor monitor = group.get().getMonitors().get(i);
                monitorsGroup.setName(monitor.getName());
                monitorsGroup.setMadeForMonitor(true);
                monitorGroupRepository.save(monitorsGroup);
                monitor.setGroup(monitorsGroup);
                monitorRepository.save(monitor);
            }
            monitorGroupRepository.deleteById(id);

            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(ADDLOGERROR);
            }
            else {
                logger.info(ADDLOGSUCCESS, description);
            }
        }
        else {
            logger.warn("Group with ID {} not found", id);
        }
    }

    /**
     * Retrieves and returns a list of all MonitorsGroups stored in the database.
     *
     * @return A list of all MonitorsGroups stored in the database.
     */
    public List<MonitorsGroup> getAllGroups() {
        String operation = "getAllGroups";
        String description = "Getting all groups";
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }
        return monitorGroupRepository.findAllByMonitorsPendingFalseOrMonitorsIsEmpty();
    }

    /**
     * Retrieves and returns a MonitorsGroup by its name.
     *
     * @param name The name of the MonitorsGroup to retrieve.
     * @return The MonitorsGroup with the specified name.
     */
    public MonitorsGroup getGroupByName(String name) {
        String operation = "getGroupByName";
        String description = "Getting group by name " + name;

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return monitorGroupRepository.findByName(name);
    }    


    /**
     * Updates a MonitorsGroup with the specified ID.
     *
     * @param id The ID of the MonitorsGroup to update.
     * @param monitorsGroup The MonitorsGroup with the new values.
     * @return The updated MonitorsGroup.
     */
    public MonitorsGroup updateGroup(Long id, MonitorsGroup monitorsGroup) {
        String operation = "updateGroup";
        String description = "Updating group by id " + id;

        MonitorsGroup monitorsGroupById = monitorGroupRepository.getReferenceById(id);
        monitorsGroupById.setName(monitorsGroup.getName());
        monitorsGroupById.setDescription(monitorsGroup.getDescription());

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return monitorGroupRepository.save(monitorsGroupById);
    }

    /**
     * Retrieves and returns a list of all MonitorsGroups that are not made for a unique monitor.
     *
     * @return A list of all MonitorsGroups that are not made for a unique monitor.
     */
    public List<MonitorsGroup> getAllGroupsNotMadeForMonitor() {
        String operation = "getAllGroupsNotMadeForMonitor";
        String description = "Getting all groups not made for monitor";

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return monitorGroupRepository.findAllByMadeForMonitorFalse();
    }
}
