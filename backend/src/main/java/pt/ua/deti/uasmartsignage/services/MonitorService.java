package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.models.MonitorGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import pt.ua.deti.uasmartsignage.enums.Log;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.Monitor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final MonitorGroupService monitorGroupService;
    private final LogsService logsService;

    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);
    private final String source = this.getClass().getSimpleName();

    /**
     * Retrieves and returns a Monitor by its ID.
     *
     * @param id The ID of the Monitor to retrieve.
     * @return The Monitor with the specified ID or empty if not found.
     */
    public Optional<Monitor> getMonitorById(Long id) {
        logger.info("Retrieving monitor with ID: {}", id);
        Optional<Monitor> monitor = monitorRepository.findById(id);

        String operation = "getMonitorById";
        String description = "Retrieved monitor with ID: " + id;

        if (monitor.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return Optional.empty();
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        boolean online = logsService.keepAliveIn10min(monitor.get());
        monitor.get().setOnline(online);

        return monitor;
    }

    /**
     * Saves a Monitor to the database.
     *
     * @param monitor The Monitor to save.
     * @return The saved Monitor.
     */
    public Monitor saveMonitor(Monitor monitor) {
        String operation = "saveMonitor";
        String description = "Created monitor with name: " + monitor.getName();

        monitor.setId(null);
        Monitor savedMonitor = monitorRepository.save(monitor);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return savedMonitor;
    }

    /**
     * Deletes a Monitor from the database.
     *
     * @param id The ID of the Monitor to delete.
     * @return Boolean with true if monitor was deleted or false if it failed.
     */
    public Boolean deleteMonitor(Long id) {
        Optional<Monitor> monitor = getMonitorById(id);

        String operation = "deleteMonitor";
        String description = "Deleted monitor with ID: " + id;

        if (monitor.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return false;
        }
        
        monitorRepository.deleteById(id);
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        return true;
    }
    
    /**
     * Updates a Monitor in the database.
     *
     * @param id The ID of the Monitor to update.
     * @param monitor The Monitor to update.
     * @return The updated Monitor.
     */
    public Monitor updateMonitor(Long id, Monitor monitor) {
        String operation = "updateMonitor";
        String description = "Updated monitor with ID: " + id + "; And name: " + monitor.getName();

        Monitor oldMonitor = getMonitorById(id).get();
        MonitorGroup oldGroup = oldMonitor.getGroup();

        MonitorGroup group = null;
        if(monitor.getGroup().getId() != null){
            group = monitorGroupService.getGroupById(monitor.getGroup().getId()).get();
        }

        if(group == null){
            monitor.setGroup(oldGroup);
        }
        else{
            monitor.setGroup(group);
        }

        monitor.setId(id);
        Monitor updatedMonitor = monitorRepository.save(monitor);

        if (oldGroup.isDefaultGroup()) {
            monitorGroupService.deleteGroupById(oldGroup.getId());
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        return updatedMonitor;
    }

    /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
     */
    public Monitor updatePending(Long id, boolean pending){
        String operation = "updatePending";
        String description = "Updated monitor with ID: " + id + "; To pending: " + pending;

        Monitor monitorById = monitorRepository.getReferenceById(id);
        monitorById.setPending(pending);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return monitorRepository.save(monitorById);
    }
    
    /**
     * Retrieves and returns all Monitors based on PENDING.
     * 
     * @param pending The PENDING status of the Monitors.
     * @return A list of all Monitors.
     */
    public List<Monitor> getAllMonitorsByPending(boolean pending) {
        String operation = "getAllMonitorsByPending";
        String description = "Retrieved all monitors by pending: " + pending;

        List<Monitor> monitors = monitorRepository.findByPending(pending);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return monitors;
    }

    /**
     * Retrieves and returns all Monitors based on PENDING and filtered by ONLINE status.
     * 
     * @param pending The PENDING status of the Monitors.
     * @param online The ONLINE status of the Monitors.
     * @return A list of all Monitors.
     */
    public List<Monitor> getAllMonitorsByPendingAndOnline(boolean pending, boolean onlineStatus) {
        String operation = "getAllMonitorsByPendingAndOnline";
        String description = "Retrieved all monitors by pending " + pending + " and by status " + onlineStatus;

        List<Monitor> monitors = monitorRepository.findByPendingAndOnline(pending, onlineStatus);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return monitors;
    }

    /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
     */
    public List<Monitor> getMonitorsByGroupAndOnline(long groupId, boolean onlineStatus) {
        String operation = "getMonitorsByGroupAndOnline";
        String description = "Retrieved all monitors by group ID " + groupId + " and by status " + onlineStatus;

        List<Monitor> monitors = monitorRepository.findByPendingAndGroup_IdAndOnline(false, groupId, onlineStatus);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        
        return monitors;
    }

        /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
     */
    public List<Monitor> getMonitorsByGroup(long groupId) {
        String operation = "getMonitorsByGroup";
        String description = "Retrieved all monitors by group ID " + groupId;
        
        List<Monitor> monitors = monitorRepository.findByPendingAndGroup_Id(false,groupId);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }
        
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return monitors;
    }

    /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
    */
    public Monitor getMonitorByUUID(String uuid) {
        String operation = "getMonitorByUUID";
        String description = "Retrieved monitor with UUID " + uuid;

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return monitorRepository.findByUuid(uuid);
    }

}
