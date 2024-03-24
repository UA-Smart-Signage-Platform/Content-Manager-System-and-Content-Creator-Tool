package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.TemplateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;


@Service
public class TemplateGroupService {

    @Autowired
    private TemplateGroupRepository templateGroupRepository;

    public TemplateGroup getGroupById(Long id) {
        return templateGroupRepository.findById(id).orElse(null);
    }

    public TemplateGroup saveGroup(TemplateGroup templateGroup) {
        return templateGroupRepository.save(templateGroup);
    }

    public void deleteGroup(Long id) {
        templateGroupRepository.deleteById(id);
    }

    public Iterable<TemplateGroup> getAllGroups() {
        return templateGroupRepository.findAll();
    }

    
    
}