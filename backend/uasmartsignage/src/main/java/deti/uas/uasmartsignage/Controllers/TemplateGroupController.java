package deti.uas.uasmartsignage.Controllers;

import java.util.List;

import deti.uas.uasmartsignage.Models.TemplateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.uas.uasmartsignage.Services.TemplateGroupService;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/templateGroups")
public class TemplateGroupController {

    private TemplateGroupService templateGroupService;

    @GetMapping
    public ResponseEntity<?> getAllTemplateGroups() {
        List<TemplateGroup> templateGroups = (List<TemplateGroup>) templateGroupService.getAllGroups();
        return new ResponseEntity<>(templateGroups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplateGroupById(@PathVariable("id") Long id) {
        TemplateGroup templateGroup = templateGroupService.getGroupById(id);
        if (templateGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(templateGroup, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveTemplateGroup(@RequestBody TemplateGroup templateGroup) {
        TemplateGroup savedTemplateGroup = templateGroupService.saveGroup(templateGroup);
        return new ResponseEntity<>(savedTemplateGroup, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplateGroup(@PathVariable("id") Long id) {
        templateGroupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplateGroup(@PathVariable("id") Long id, @RequestBody TemplateGroup templateGroup) {
        TemplateGroup updatedTemplateGroup = templateGroupService.updateTemplateGroup(id, templateGroup);
        if (updatedTemplateGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTemplateGroup, HttpStatus.OK);
    }
    
}
