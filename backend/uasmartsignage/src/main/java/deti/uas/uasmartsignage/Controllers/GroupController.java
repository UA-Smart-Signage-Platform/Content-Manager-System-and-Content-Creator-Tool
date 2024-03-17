package deti.uas.uasmartsignage.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.uas.uasmartsignage.Models.Group;
import deti.uas.uasmartsignage.Services.GroupService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class GroupController {

    private GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<?> getAllGroups() {
        List<Group> groups = (List<Group>) groupService.getAllGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<?> getGroupById(Long id) {
        Group group = groupService.getGroupById(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @GetMapping("/groups/name/{name}")
    public ResponseEntity<?> getGroupByName(String name) {
        Group group = groupService.getGroupByName(name);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @GetMapping("/groups/{id}/screens")
    public ResponseEntity<?> getScreensByGroup(Long id) {
        Group group = groupService.getGroupById(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group.getScreens(), HttpStatus.OK);
    }

    @GetMapping("/groups/{id}/template")
    public ResponseEntity<?> getTemplateByGroup(Long id) {
        Group group = groupService.getGroupById(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group.getTemplate_group(), HttpStatus.OK);
    }
    
}
