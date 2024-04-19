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

    public MonitorsGroup getGroupById(Long id) {
        return monitorGroupRepository.findById(id).orElse(null);
    }

    public MonitorsGroup saveGroup(MonitorsGroup monitorsGroup) {
        return monitorGroupRepository.save(monitorsGroup);
    }

    public void deleteGroup(Long id) {
        Optional<MonitorsGroup> group = monitorGroupRepository.findById(id);
        if (group.isPresent()) {
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

    public MonitorsGroup getGroupByName(String name) {
        return monitorGroupRepository.findByName(name);
    }    

    public MonitorsGroup updateGroup(Long id, MonitorsGroup monitorsGroup) {
        MonitorsGroup monitorsGroupById = monitorGroupRepository.getReferenceById(id);
        monitorsGroupById.setName(monitorsGroup.getName());
        monitorsGroupById.setDescription(monitorsGroup.getDescription());
        return monitorGroupRepository.save(monitorsGroupById);
    }

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
