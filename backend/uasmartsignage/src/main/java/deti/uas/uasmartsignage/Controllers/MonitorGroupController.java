package deti.uas.uasmartsignage.Controllers;

import java.util.List;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.uas.uasmartsignage.Services.MonitorGroupService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/groups")
public class MonitorGroupController {

    private MonitorGroupService monitorGroupService;

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        List<MonitorsGroup> monitorsGroups = (List<MonitorsGroup>) monitorGroupService.getAllGroups();
        return new ResponseEntity<>(monitorsGroups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable("id") Long id) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getGroupByName(@PathVariable("name") String name) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupByName(name);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup, HttpStatus.OK);
    }

    @GetMapping("/{id}/screens")
    public ResponseEntity<?> getScreensByGroup(@PathVariable("id") Long id){
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup.getMonitors(), HttpStatus.OK);
    }

    @GetMapping("/{id}/template")
    public ResponseEntity<?> getTemplateByGroup(@PathVariable("id") Long id) {
        MonitorsGroup monitorsGroup = monitorGroupService.getGroupById(id);
        if (monitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(monitorsGroup.getTemplateGroup(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody MonitorsGroup monitorsGroup) {
        MonitorsGroup newMonitorsGroup = monitorGroupService.saveGroup(monitorsGroup);
        if (newMonitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newMonitorsGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable("id") Long id, @RequestBody MonitorsGroup monitorsGroup) {
        MonitorsGroup updatedMonitorsGroup = monitorGroupService.updateGroup(id, monitorsGroup);
        if (updatedMonitorsGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedMonitorsGroup, HttpStatus.OK);
    }
    
}
