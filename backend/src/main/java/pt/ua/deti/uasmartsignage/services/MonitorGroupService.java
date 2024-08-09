package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.dto.MonitorGroupDTO;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import org.springframework.stereotype.Service;

import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MonitorGroupService {

    private final MonitorGroupRepository monitorGroupRepository;
    private final MonitorService monitorService;

    public List<MonitorGroup> getAllGroups() {
        return monitorGroupRepository.findAllByMonitorsIsEmptyOrMonitorsPendingFalse();
    }

    public List<MonitorGroup> getAllNonDefaultGroups() {
        return monitorGroupRepository.findAllByMonitorsIsEmptyOrIsDefaultGroupFalse();
    }

    public MonitorGroup getGroupById(Long id) {
        return monitorGroupRepository.findById(id).orElse(null);
    }

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

        MonitorGroup group = getGroupById(id);
        if (group != null) {
            if (group.getMonitors().isEmpty()) {
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
                monitorService.updateMonitor(monitor.getId(), monitor);
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
            Monitor monitor = monitorService.getMonitorById(monitorId);
            if (monitor == null)
                return null;
            monitors.add(monitor);
        }

        group.setMonitors(monitors);
        return group;
    }
}
