package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonitorGroupService {

    private final MonitorGroupRepository monitorGroupRepository;

    private final MonitorRepository monitorRepository;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MonitorGroupService.class);

    public MonitorGroupService(MonitorGroupRepository monitorGroupRepository, MonitorRepository monitorRepository){
        this.monitorGroupRepository = monitorGroupRepository;
        this.monitorRepository = monitorRepository;
    }

    /**
     * Retrieves and returns a MonitorsGroup by its ID.
     *
     * @param id The ID of the MonitorsGroup to retrieve.
     * @return The MonitorsGroup with the specified ID.
     */
    public MonitorsGroup getGroupById(Long id) {
        return monitorGroupRepository.findById(id).orElse(null);
    }

    /**
     * Saves a MonitorsGroup to the database.
     *
     * @param monitorsGroup The MonitorsGroup to save.
     * @return The saved MonitorsGroup.
     */
    public MonitorsGroup saveGroup(MonitorsGroup monitorsGroup) {
        return monitorGroupRepository.save(monitorsGroup);
    }

    /**
     * Deletes a MonitorsGroup from the database and creates new groups for all the monitors that the group had.
     *
     * @param id The ID of the MonitorsGroup to delete.
     */
    public void deleteGroup(Long id) {
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
        List<MonitorsGroup> groups = monitorGroupRepository.findAll();
        List<MonitorsGroup> groupsWithoutPendingMonitors = new ArrayList<>();
        for (MonitorsGroup group : groups) {
            for (int i = 0; i < group.getMonitors().size(); i++) {
                if (group.getMonitors().get(i).isPending()) {
                    break;
                }
                if (i == group.getMonitors().size() - 1) {
                    groupsWithoutPendingMonitors.add(group);
                }
            }
        }
        return groupsWithoutPendingMonitors;
    }

    /**
     * Retrieves and returns a MonitorsGroup by its name.
     *
     * @param name The name of the MonitorsGroup to retrieve.
     * @return The MonitorsGroup with the specified name.
     */
    public MonitorsGroup getGroupByName(String name) {
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
        MonitorsGroup monitorsGroupById = monitorGroupRepository.getReferenceById(id);
        monitorsGroupById.setName(monitorsGroup.getName());
        monitorsGroupById.setDescription(monitorsGroup.getDescription());
        return monitorGroupRepository.save(monitorsGroupById);
    }

    /**
     * Retrieves and returns a list of all MonitorsGroups that are not made for a unique monitor.
     *
     * @return A list of all MonitorsGroups that are not made for a unique monitor.
     */
    public List<MonitorsGroup> getAllGroupsNotMadeForMonitor() {
        List<MonitorsGroup> groups = monitorGroupRepository.findAll();
        List<MonitorsGroup> groupsNotMadeForMonitor = new ArrayList<>();
        for (MonitorsGroup group : groups) {
            if (!group.isMadeForMonitor()) {
                groupsNotMadeForMonitor.add(group);
            }
        }
        if (groups.isEmpty()) {
            logger.warn("No groups found");
            return new ArrayList<>();
        }
        return groupsNotMadeForMonitor;
    }
}
