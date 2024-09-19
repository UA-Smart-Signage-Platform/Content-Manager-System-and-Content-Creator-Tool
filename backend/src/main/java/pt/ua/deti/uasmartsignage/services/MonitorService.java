package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import pt.ua.deti.uasmartsignage.models.Rule;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import pt.ua.deti.uasmartsignage.models.Monitor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final MonitorGroupService monitorGroupService;
    private final RuleService ruleService;
    private final LogsService logsService;

    /**
     * Retrieves and returns a Monitor by its ID.
     *
     * @param id The ID of the Monitor to retrieve.
     * @return The Monitor with the specified ID.
     */
    public Monitor getMonitorById(Long id) {

        Monitor monitor = monitorRepository.findById(id).orElse(null);

        if (monitor == null) {
            return null;
        }

        boolean online = logsService.keepAliveIn10min(monitor);
        monitor.setOnline(online);

        return monitor;
    }

    /**
     * Saves a Monitor to the database.
     *
     * @param monitor The Monitor to save.
     * @return The saved Monitor.
     */
    public Monitor saveMonitor(Monitor monitor) {
        monitor.setId(null);
        return monitorRepository.save(monitor);
    }

    /**
     * Deletes a Monitor from the database.
     *
     * @param id The ID of the Monitor to delete.
     */
    public void deleteMonitor(Long id) {
        monitorRepository.deleteById(id);
    }
    
    /**
     * Updates a Monitor in the database.
     *
     * @param id The ID of the Monitor to update.
     * @param monitor The Monitor to update.
     * @return The updated Monitor.
     */
    public Monitor updateMonitor(Long id, Monitor monitor) {

        Monitor oldMonitor = getMonitorById(id);
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

        return updatedMonitor;
    }

    /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
     */
    public Monitor updatePending(Long id,boolean pending){

        Monitor monitorById = monitorRepository.getReferenceById(id);
        monitorById.setPending(pending);

        return monitorRepository.save(monitorById);
    }
    
    /**
     * Retrieves and returns all Monitors based on PENDING.
     * 
     * @param pending The PENDING status of the Monitors.
     * @return A list of all Monitors.
     */
    public List<Monitor> getAllMonitorsByPending(boolean pending) {

        List<Monitor> monitors = monitorRepository.findByPending(pending);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }
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
        String description = "Getting all monitors by pending " + pending + " and by status " + onlineStatus;

        List<Monitor> monitors = monitorRepository.findByPendingAndOnline(pending, onlineStatus);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }

        // if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
        //     logger.error(ADDLOGERROR);
        // }
        // else {
        //     logger.info(ADDLOGSUCCESS, description);
        // }
        return monitors;
    }

    /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
     */
    public List<Monitor> getMonitorsByGroupAndOnline(long groupId, boolean onlineStatus) {
        String operation = "getMonitorsByGroupAndOnline";
        String description = "Getting all monitors by group " + groupId + " and by status " + onlineStatus;

        List<Monitor> monitors = monitorRepository.findByPendingAndGroup_IdAndOnline(false, groupId, onlineStatus);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }


        // if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
        //     logger.error(ADDLOGERROR);
        // }
        // else {
        //     logger.info(ADDLOGSUCCESS, description);
        // }
        
        return monitors;
    }

        /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
     */
    public List<Monitor> getMonitorsByGroup(long groupId) {

        List<Monitor> monitors = monitorRepository.findByPendingAndGroup_Id(false,groupId);

        for (Monitor monitor : monitors) {
            boolean online = logsService.keepAliveIn10min(monitor);
            monitor.setOnline(online);
        }
        
        return monitors;
    }

    /**
     * Retrieves and returns all Monitors.
     *
     * @return A list of all Monitors.
    */
    public Monitor getMonitorByUUID(String uuid) {
        return monitorRepository.findByUuid(uuid);
    }

}
