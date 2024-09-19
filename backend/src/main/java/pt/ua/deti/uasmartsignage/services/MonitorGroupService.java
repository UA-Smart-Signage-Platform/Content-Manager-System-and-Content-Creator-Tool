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
        logger.info("Retrieving group with ID: {}", id);
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
    public Optional<MonitorGroup> getGroupByName(String name){
        logger.info("Retrieving group with name: {}", name);
        Optional<MonitorGroup> group = monitorGroupRepository.findByName(name);

        String operation = "getGroupByName";
        String description = "Retrieved group with name: " + name;

        if (group.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(name);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return Optional.empty();
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return group;
    }


    /**
     * Saves group based on Data Transfer Object (DTO).
     * 
     * @param groupDTO The DTO with information necessary to create a group.
     * @return The newly created MonitorGroup, or null if no group was created.
    */
    public MonitorGroup saveGroup(MonitorGroupDTO groupDTO) {
        MonitorGroup group = convertDTOToMonitorGroup(groupDTO);

        String operation = "saveGroup(DTO)";
        String description = "Created group with name: " + groupDTO.getName();

        if (group == null){
            description = "Failed to create group with name: " + groupDTO.getName();
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        }

        group.setId(null);
        MonitorGroup savedGroup = monitorGroupRepository.save(group);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return savedGroup;
    }

    /**
     * Saves group based on group object.
     * 
     * @param group The MonitorGroup object with information necessary to create a group.
     * @return The newly created MonitorGroup.
    */
    public MonitorGroup saveGroup(MonitorGroup group) {
        String operation = "saveGroup";
        String description = "Created group with name: " + group.getName();

        group.setId(null);
        MonitorGroup savedGroup = monitorGroupRepository.save(group);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return savedGroup;
    }


    /**
     * Updates group based on ID and DTO.
     * 
     * @param id The ID of the desired group to update.
     * @param groupDTO The DTO with information necessary to update the group.
     * @return The updated MonitorGroup.
    */
    public MonitorGroup updateGroup(Long id, MonitorGroupDTO groupDTO) {
        MonitorGroup group = convertDTOToMonitorGroup(groupDTO);

        String operation = "updateGroup(DTO)";
        String description = "Updated group with ID: " + id + "; And name: " + groupDTO.getName();

        if (group == null){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        }

        group.setId(id);
        MonitorGroup updatedGroup = monitorGroupRepository.save(group);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return updatedGroup;
    }


    /**
     * Updates group based on ID and group object.
     * 
     * @param id The ID of the desired group to update.
     * @param group The MonitorGroup object with information necessary to update the desired group.
     * @return The updated MonitorGroup.
    */
    public MonitorGroup updateGroup(Long id, MonitorGroup group) {
        String operation = "updateGroup";
        String description = "Updated group with ID: " + id + "; And name: " + group.getName();

        group.setId(id);
        MonitorGroup updatedGroup = monitorGroupRepository.save(group);

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return updatedGroup;
    }


    /**
     * Deletes group based on ID and separates monitors that were part of the group into different individual groups
     * 
     * @param id The ID of the desired group to delete.
     * @return Boolean with true if group was deleted or false if it failed.
    */
    public boolean deleteGroupById(Long id) {
        Optional<MonitorGroup> group = getGroupById(id);

        String operation = "deleteGroupById";
        String description = "Deleted group with ID: " + id;

        if (group.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return false;
        }

        MonitorGroup groupToDelete = group.get();
        if (groupToDelete.getMonitors().isEmpty() || groupToDelete.isDefaultGroup()) {
            monitorGroupRepository.deleteById(id);
            logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
            return true;
        }

        // separate all the monitors that were part of the group
        // into different individual groups
        for (Monitor monitor : groupToDelete.getMonitors()) {

            MonitorGroup newGroup = MonitorGroup.builder()
                                                .name(monitor.getName())
                                                .isDefaultGroup(true)
                                                .build();

            newGroup = saveGroup(newGroup);
            monitor.setGroup(newGroup);
            monitorRepository.save(monitor);
            monitorGroupRepository.deleteById(id);
        }

        logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
        return true;
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
