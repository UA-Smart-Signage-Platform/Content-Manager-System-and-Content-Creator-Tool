package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import pt.ua.deti.uasmartsignage.models.Rule;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import pt.ua.deti.uasmartsignage.models.Monitor;

import java.util.ArrayList;
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

        Monitor monitorById = monitorRepository.getReferenceById(id);
        MonitorGroup group = monitorById.getGroup();
        monitorById.setName(monitor.getName());
        monitorById.setGroup(monitor.getGroup());
        Monitor returnMonitor = monitorRepository.save(monitorById);

        if (group.isDefaultGroup()) {
            if (group.getId() == monitorById.getGroup().getId()){
                group.setName(monitor.getName());
                monitorGroupService.saveGroup(group);
            }
            else{
                List<Rule> rules = ruleService.getAllRulesForGroup(group.getId());
                if (rules.isEmpty()){
                    monitorGroupService.deleteGroupById(group.getId());
                } else {
                    for (int i = 0; i < rules.size(); i++) {
                        ruleService.deleteRuleById(rules.get(i).getId());
                    }
                    monitorGroupService.deleteGroupById(group.getId());
                }
            }
        }

        // Send all schedules to the new monitor
        List<Monitor> monitors = new ArrayList<>();
        monitors.add(returnMonitor);
        // templateGroupService.sendAllSchedulesToMonitorGroup(monitors);

        return returnMonitor;
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
     * Retrieves and returns all Monitors.
     *
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
