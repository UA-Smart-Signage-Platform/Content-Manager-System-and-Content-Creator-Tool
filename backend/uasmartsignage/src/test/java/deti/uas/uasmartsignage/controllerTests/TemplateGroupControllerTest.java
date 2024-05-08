package deti.uas.uasmartsignage.controllerTests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;


import deti.uas.uasmartsignage.Models.*;
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

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import deti.uas.uasmartsignage.Controllers.TemplateGroupController;
import org.springframework.test.web.servlet.ResultActions;

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
    @Disabled  //problema com o mqtt
    void testSaveTemplateGroupEndpoint() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setPending(false);
        monitor.setWidth(1);
        monitor.setHeight(1);

        Monitor monitor1 = new Monitor();
        monitor1.setName("monitor1");
        monitor1.setPending(false);
        monitor1.setWidth(12);
        monitor1.setHeight(12);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");
        group.setMonitors(List.of(monitor,monitor1));

        Widget widget = new Widget();
        widget.setName("widget1");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setWidget(widget);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setWidget(widget);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));


        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);

        ResultActions result = mvc.perform(post("/api/templateGroups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isCreated());

       // Extract the content from the response
        String contentAsString = result.andReturn().getResponse().getContentAsString();

        mvc.perform(post("/api/templateGroups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.group.name", is("group1")));
                //.andExpect(jsonPath("$.template.name", is("template1")));
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
    @Disabled // //problema com o mqtt
    void testUpdateTemplateGroupEndpoint() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setPending(false);
        monitor.setWidth(1);
        monitor.setHeight(1);

        Monitor monitor1 = new Monitor();
        monitor1.setName("monitor1");
        monitor1.setPending(false);
        monitor1.setWidth(12);
        monitor1.setHeight(12);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");
        group.setMonitors(List.of(monitor,monitor1));

        Widget widget = new Widget();
        widget.setName("widget1");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setWidget(widget);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setWidget(widget);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));


        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);

        when(templateService.getTemplateById(1L)).thenReturn(template);
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(service.getGroupById(1L)).thenReturn(templateGroup);
        when(service.saveGroup(Mockito.any(TemplateGroup.class))).thenReturn(templateGroup);

        mvc.perform(put("/api/templateGroups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("content2")));
    }

    @Test
    @Disabled // problem with mqtt
    void testSetTemplateForTemplateGroupEndpoint() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor");
        monitor.setPending(false);
        monitor.setWidth(1);
        monitor.setHeight(1);

        Monitor monitor1 = new Monitor();
        monitor1.setName("monitor1");
        monitor1.setPending(false);
        monitor1.setWidth(12);
        monitor1.setHeight(12);

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");
        group.setMonitors(List.of(monitor,monitor1));

        Widget widget = new Widget();
        widget.setName("widget1");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setWidget(widget);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setWidget(widget);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));


        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        when(service.getGroupById(1L)).thenReturn(templateGroup);
        when(service.saveGroup(Mockito.any(TemplateGroup.class))).thenReturn(templateGroup);

        mvc.perform(put("/api/templateGroups/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.template.name", is("template2")));
    }
}
