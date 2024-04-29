package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import deti.uas.uasmartsignage.Models.*;
import deti.uas.uasmartsignage.Services.ContentService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import deti.uas.uasmartsignage.Services.TemplateService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;
import deti.uas.uasmartsignage.Services.TemplateGroupService;

@ExtendWith(MockitoExtension.class)
class TemplateGroupServiceTest {

    @Mock
    private TemplateGroupRepository repository;

    @Mock
    private TemplateService templateService;

    @Mock
    private MonitorGroupService groupService;

    @Mock
    private ContentService contentService;

    @InjectMocks
    private TemplateGroupService service;



    @Test
    void testGetTemplateGroupByIdReturnsTemplateGroup(){
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(new Template());
        when(repository.findById(1L)).thenReturn(Optional.of(templateGroup));

        TemplateGroup template = service.getGroupById(1L);

        assertThat(template.getGroup()).isEqualTo(group);
    }

    @Test
    void testGetTemplateGroupByGroupID(){
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        MonitorsGroup group2 = new MonitorsGroup();
        group2.setName("group2");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group2);
        templateGroup.setTemplate(new Template());
        when(repository.findByGroupId(group2.getId())).thenReturn(templateGroup);

        TemplateGroup template = service.getTemplateGroupByGroupID(group2.getId());

        assertThat(template.getGroup()).isEqualTo(group2);
    }

    @Test
    void testSaveTemplateGroup(){
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setPending(false);

        Monitor monitor1 = new Monitor();
        monitor1.setName("monitor1");
        monitor1.setPending(false);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");
        group.setMonitors(List.of(monitor,monitor1));

        Template template = new Template();
        template.setName("template1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        when(repository.save(templateGroup)).thenReturn(templateGroup);

        TemplateGroup saved_template = service.saveGroup(templateGroup);

        assertThat(saved_template).isEqualTo(templateGroup);
    }

    @Test
    void testDeleteTemplateGroup(){
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Template template = new Template();
        template.setName("template1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        doNothing().when(repository).deleteById(1L);

        service.deleteGroup(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateTemplateGroup(){
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        //monitor.setUuid("1c832f8c-1f6b-4722-a693-a3956b0cbbc9");
        monitor.setPending(false);

        Monitor monitor1 = new Monitor();
        monitor1.setName("monitor1");
        //monitor1.setUuid("1c832f8c-1f6b-4722-a694-a3956b0cbbc9");
        monitor1.setPending(false);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");
        group.setMonitors(List.of(monitor,monitor1));

        MonitorsGroup group2 = new MonitorsGroup();
        group2.setName("group2");
        group2.setMonitors(List.of(monitor1));

        Template template = new Template();
        template.setName("template1");


        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        templateGroup.setContent(Map.of(1, "content1"));
        when(repository.findById(1L)).thenReturn(Optional.of(templateGroup));


        TemplateGroup templateGroup2 = new TemplateGroup();
        templateGroup2.setGroup(group2);
        templateGroup2.setContent(Map.of(2, "content2"));
        templateGroup2.setTemplate(template);
        when(repository.save(templateGroup)).thenReturn(templateGroup2);

        TemplateGroup updated_template = service.updateTemplateGroup(1L, templateGroup2);

        assertThat(updated_template.getGroup()).isEqualTo(group2);
    }





}
