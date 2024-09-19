package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.dto.MonitorGroupDTO;
import pt.ua.deti.uasmartsignage.enums.Log;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MonitorGroupService {

    private final MonitorGroupRepository monitorGroupRepository;
    private final MonitorRepository monitorRepository;
    private final LogsService logsService;

    private static final Logger logger = LoggerFactory.getLogger(MonitorGroupService.class);
    private final String source = this.getClass().getSimpleName();


    /**
     * Retrieves and returns a list of all existing monitor groups.
     * 
     * @return A list of all groups.
    */
    public List<MonitorGroup> getAllGroups() {
        List<MonitorGroup> groups = monitorGroupRepository.findAllByMonitorsIsEmptyOrMonitorsPendingFalse();

        String operation = "getAllGroups";
        String description = "Retrieved " + groups.size() + " groups.";
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return groups;
    }


    /**
     * Retrieves and returns a list of all non default monitor groups.
     * 
     * @return A list of all non default groups.
    */
    public List<MonitorGroup> getAllNonDefaultGroups() {
        List<MonitorGroup> groups = monitorGroupRepository.findAllByMonitorsIsEmptyOrIsDefaultGroupFalse();

        String operation = "getAllNonDefaultGroups";
        String description = "Retrieved " + groups.size() + " non-default groups.";
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return groups;
    }


    /**
     * Retrieves and returns MonitorGroup with the specified ID from the monitorGroup repository.
     * 
     * @param id The ID of the MonitorGroup to retrieve.
     * @return The MonitorGroup with the specified ID, or null if no such group is found.
     */
    public Optional<MonitorGroup> getGroupById(Long id) {
        logger.info("Retrieving file with ID: {}", id);
        Optional<MonitorGroup> group = monitorGroupRepository.findById(id);

        String operation = "getGroupById";
        String description = "Retrieved group with ID: " + id;

        if (group.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return Optional.empty();
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return group;
    }


    /**
     * Retrieves and returns MonitorGroup with the specified name from the monitorGroup repository.
     * 
     * @param name The name of the MonitorGroup to retrieve.
     * @return The MonitorGroup with the specified name, or null if no such group is found.
     */
    public MonitorGroup getGroupByName(String name){
        return monitorGroupRepository.findByName(name);
    }

    public MonitorGroup saveGroup(MonitorGroupDTO groupDTO) {
        MonitorGroup group = convertDTOToMonitorGroup(groupDTO);
        if (group == null){
            return null;
        }
        group.setId(null);
        return monitorGroupRepository.save(group);
    }

    public MonitorGroup saveGroup(MonitorGroup group) {
        group.setId(null);
        return monitorGroupRepository.save(group);
    }

    public MonitorGroup updateGroup(Long id, MonitorGroupDTO groupDTO) {
        MonitorGroup group = convertDTOToMonitorGroup(groupDTO);
        if (group == null){
            return null;
        }
        group.setId(id);
        return monitorGroupRepository.save(group);
    }

    public MonitorGroup updateGroup(Long id, MonitorGroup group) {
        group.setId(id);
        return monitorGroupRepository.save(group);
    }

    public void deleteGroupById(Long id) {

        MonitorGroup group = getGroupById(id).get();
        if (group != null) {
            if (group.getMonitors().isEmpty() || group.isDefaultGroup()) {
                monitorGroupRepository.deleteById(id);
                return;
            }
            // separate all the monitors that were part of the group
            // into different individual groups
            for (Monitor monitor : group.getMonitors()) {

                MonitorGroup newGroup = MonitorGroup.builder()
                                                    .name(monitor.getName())
                                                    .isDefaultGroup(true)
                                                    .build();

                newGroup = saveGroup(newGroup);
                monitor.setGroup(newGroup);
                monitorRepository.save(monitor);
                monitorGroupRepository.deleteById(id);
            }
        }
    }

    public MonitorGroup convertDTOToMonitorGroup(MonitorGroupDTO groupDTO){
        MonitorGroup group = MonitorGroup.builder()
                                        .name(groupDTO.getName())
                                        .description(groupDTO.getDescription())
                                        .build();

        if (groupDTO.getMonitorIds() == null){
            return group;
        }

        List<Monitor> monitors = new ArrayList<>();
        for(Long monitorId : groupDTO.getMonitorIds()){
            Monitor monitor = monitorRepository.findById(monitorId).get();
            if (monitor == null)
                return null;
            monitors.add(monitor);
        }

        group.setMonitors(monitors);
        return group;
    }
}
