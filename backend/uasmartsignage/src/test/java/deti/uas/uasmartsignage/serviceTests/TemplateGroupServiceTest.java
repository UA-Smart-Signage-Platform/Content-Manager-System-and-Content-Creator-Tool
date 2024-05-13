package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;
import deti.uas.uasmartsignage.Services.LogsService;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.MonitorsGroup;


@ExtendWith(MockitoExtension.class)
class TemplateGroupServiceTest {

    @Mock
    private TemplateGroupRepository repository;

    @Mock
    private LogsService logsService;

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
}