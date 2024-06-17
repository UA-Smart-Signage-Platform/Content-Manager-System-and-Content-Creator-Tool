package deti.uas.uasmartsignage.controllerTests;

import static org.mockito.Mockito.when;

import java.util.*;

import deti.uas.uasmartsignage.Models.*;
import deti.uas.uasmartsignage.Services.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;
import deti.uas.uasmartsignage.Controllers.TemplateGroupController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateGroupController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
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

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;


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
    @Disabled("this is not working because as the service was made is not possible to mock the mqtt service")
    void testSaveTemplateGroupEndpoint() throws Exception{
        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(7);
        schedule1.setPriority(1);

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

        Content content = new Content();
        content.setName("Content1");
        content.setType("text");

        Widget widget = new Widget();
        widget.setName("widget1");
        widget.setId(1L);
        widget.setContents(List.of(content));
        widget.setPath("path");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setId(100L);
        templateWidget.setWidget(widget);
        templateWidget.setZIndex(1);
        templateWidget.setTop(1);
        templateWidget.setLeftPosition(1);
        templateWidget.setWidth(1);
        templateWidget.setHeight(1);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setId(200L);
        templateWidget1.setWidget(widget);
        templateWidget1.setZIndex(2);
        templateWidget1.setTop(2);
        templateWidget1.setLeftPosition(2);
        templateWidget1.setWidth(2);
        templateWidget1.setHeight(2);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        templateGroup.setSchedule(schedule1);
        templateGroup.setContent(Map.of(1,"Content1"));

        group.setTemplateGroups(List.of(templateGroup));

        mvc.perform(post("/api/templateGroups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isCreated());
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
    @Disabled("this is not working because as the service was made is not possible to mock the mqtt service")
    void testUpdateTemplateGroupEndpoint() throws Exception{
        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(7);
        schedule1.setPriority(1);

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

        Content content = new Content();
        content.setName("Content1");
        content.setType("text");

        Widget widget = new Widget();
        widget.setName("widget1");
        widget.setId(1L);
        widget.setContents(List.of(content));
        widget.setPath("path");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setId(100L);
        templateWidget.setWidget(widget);
        templateWidget.setZIndex(1);
        templateWidget.setTop(1);
        templateWidget.setLeftPosition(1);
        templateWidget.setWidth(1);
        templateWidget.setHeight(1);


        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setId(200L);
        templateWidget1.setWidget(widget);
        templateWidget1.setZIndex(2);
        templateWidget1.setTop(2);
        templateWidget1.setLeftPosition(2);
        templateWidget1.setWidth(2);
        templateWidget1.setHeight(2);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        templateGroup.setSchedule(schedule1);
        templateGroup.setContent(Map.of(1,"Content1"));

        Template template1 = new Template();
        template.setName("template2");
        template.setTemplateWidgets(List.of(templateWidget,templateWidget1));

        TemplateGroup updated_templateGroup = new TemplateGroup();
        updated_templateGroup.setGroup(group);
        updated_templateGroup.setTemplate(template1);
        updated_templateGroup.setSchedule(schedule1);
        updated_templateGroup.setContent(Map.of(1,"Content_update"));

        group.setTemplateGroups(List.of(templateGroup));

        when(repository.findById(1L)).thenReturn(Optional.of(templateGroup));
        when(service.updateTemplateGroup(1L,updated_templateGroup)).thenReturn(updated_templateGroup);


        mvc.perform(put("/api/templateGroups/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("content2")));
    }


    @Test
    void testDeleteTemplateGroups() throws Exception{
        Schedule schedule = new Schedule();
        schedule.setPriority(1);

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
        group.setMonitors(List.of(monitor, monitor1));

        Widget widget = new Widget();
        widget.setName("widget1");

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setWidget(widget);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setWidget(widget);

        Template template = new Template();
        template.setName("template1");
        template.setTemplateWidgets(List.of(templateWidget, templateWidget1));

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setGroup(group);
        templateGroup.setTemplate(template);
        templateGroup.setSchedule(schedule);

        TemplateGroup templateGroup1 = new TemplateGroup();
        templateGroup1.setGroup(group);
        templateGroup1.setTemplate(template);
        templateGroup1.setSchedule(schedule);

        when(service.getGroupById(1L)).thenReturn(templateGroup);
        when(service.getGroupById(2L)).thenReturn(templateGroup1);

        List<Integer> templateGroupsIds = Arrays.asList(1, 2);

        mvc.perform(delete("/api/templateGroups/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateGroupsIds)))
                .andExpect(status().isNoContent());
    }
}
