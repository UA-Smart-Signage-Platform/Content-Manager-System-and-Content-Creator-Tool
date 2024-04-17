package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;

@Service
public class MonitorGroupService {

    @Autowired
    private MonitorGroupRepository monitorGroupRepository;

    public MonitorsGroup getGroupById(Long id) {
        return monitorGroupRepository.findById(id).orElse(null);
    }

    public MonitorsGroup saveGroup(MonitorsGroup monitorsGroup) {
        return monitorGroupRepository.save(monitorsGroup);
    }

    public void deleteGroup(Long id) {
        monitorGroupRepository.deleteById(id);
    }

    public Iterable<MonitorsGroup> getAllGroups() {
        return monitorGroupRepository.findAll();
    }

    public MonitorsGroup getGroupByName(String name) {
        return monitorGroupRepository.findByName(name);
    }    

    public MonitorsGroup updateGroup(Long id, MonitorsGroup monitorsGroup) {
        MonitorsGroup monitorsGroupById = monitorGroupRepository.findById(id).orElse(null);
        if (monitorsGroupById == null) {
            return null;
        }
        monitorsGroupById.setName(monitorsGroup.getName());
        return monitorGroupRepository.save(monitorsGroupById);
    }
    
}
