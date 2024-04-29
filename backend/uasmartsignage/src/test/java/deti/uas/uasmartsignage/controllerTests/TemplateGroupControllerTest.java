package deti.uas.uasmartsignage.controllerTests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Services.ContentService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import deti.uas.uasmartsignage.Services.TemplateService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Controllers.TemplateGroupController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateGroupController.class)
@ActiveProfiles("test")
class TemplateGroupControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TemplateGroupService service;

    @MockBean
    private TemplateGroupRepository repository;

    @MockBean
    private TemplateService templateService;

    @MockBean
    private MonitorGroupService groupService;

    @MockBean
    private ContentService contentService;



    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MonitorGroupService monitorGroupService;

    @Test
    void testGetAllTemplateGroupsEndpoint() throws Exception{
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        MonitorsGroup group2 = new MonitorsGroup();
        group2.setName("group2");

        Template template = new Template();
        template.setName("template1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);

        TemplateGroup templateGroup2 = new TemplateGroup();
        templateGroup2.setGroup(group2);
        templateGroup2.setTemplate(template);

        List<TemplateGroup> templateGroups = Arrays.asList(templateGroup, templateGroup2);

        when(service.getAllGroups()).thenReturn(templateGroups);

        mvc.perform(get("/api/templateGroups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].group.name", is("group1")))
                .andExpect(jsonPath("$[1].group.name", is("group2")));
    }

    @Test
    void testGetTemplateGroupByIdEndpoint() throws Exception{
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Template template = new Template();
        template.setName("template1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);

        when(service.getGroupById(1L)).thenReturn(templateGroup);

        mvc.perform(get("/api/templateGroups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.group.name", is("group1")))
                .andExpect(jsonPath("$.template.name", is("template1")));
    }

    @Test
    void testSaveTemplateGroupEndpoint() throws Exception{
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

        when(service.saveGroup(Mockito.any(TemplateGroup.class))).thenReturn(templateGroup);

        mvc.perform(post("/api/templateGroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.group.name", is("group1")))
                .andExpect(jsonPath("$.template.name", is("template1")));
    }

    @Test
    void testDeleteTemplateGroupEndpoint() throws Exception{
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Template template = new Template();
        template.setName("template1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);

        when(service.getGroupById(1L)).thenReturn(templateGroup);

        mvc.perform(delete("/api/templateGroups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Disabled
    void testUpdateTemplateGroupEndpoint() throws Exception{
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Template template = new Template();
        template.setName("template1");

        Template template2 = new Template();
        template2.setName("template2");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        templateGroup.setContent(Map.of(1, "content"));

        TemplateGroup templateGroup2 = new TemplateGroup();
        templateGroup2.setGroup(group);
        templateGroup2.setTemplate(template);
        templateGroup2.setContent(Map.of(1, "content2"));

        when(service.getGroupById(1L)).thenReturn(templateGroup);
        when(service.saveGroup(Mockito.any(TemplateGroup.class))).thenReturn(templateGroup);

        mvc.perform(put("/api/templateGroups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateGroup2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("content2")));
    }

    @Test
    @Disabled
    void testSetTemplateForTemplateGroupEndpoint() throws Exception{
        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Template template = new Template();
        template.setName("template1");

        Template template2 = new Template();
        template2.setName("template2");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);

        TemplateGroup templateGroup2 = new TemplateGroup();
        templateGroup2.setGroup(group);
        templateGroup2.setTemplate(template2);


        when(service.getGroupById(1L)).thenReturn(templateGroup);
        when(templateService.getTemplateById(1L)).thenReturn(template2);
        when(monitorGroupService.getGroupById(1L)).thenReturn(group);

        when(service.saveGroup(Mockito.any(TemplateGroup.class))).thenReturn(templateGroup2);

        mvc.perform(put("/api/templateGroups/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateGroup2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.template.name", is("template2")));
    }
}
