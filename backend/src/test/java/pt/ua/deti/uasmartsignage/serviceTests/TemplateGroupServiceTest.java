package pt.ua.deti.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import pt.ua.deti.uasmartsignage.Configuration.MqttConfig;
import pt.ua.deti.uasmartsignage.Models.*;
import pt.ua.deti.uasmartsignage.Services.*;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import pt.ua.deti.uasmartsignage.Repositories.TemplateGroupRepository;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class TemplateGroupServiceTest {


    @Mock
    private TemplateGroupRepository repository;

    @Mock
    private LogsService logsService;

    @Mock
    private ClassLoader cl;

    @Mock
    private InputStream inputStreamMock;

    @Mock
    private TemplateService templateService;

    @Mock
    private MonitorGroupService groupService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private TemplateGroupService templateGroupService;


    @Mock
    private TemplateWidgetService templateWidgetService;

    @Mock
    private IMqttClient ImqttClient;

    @Mock
    private MqttConfig mqttConfig;


    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TemplateGroupService service;




    @Test
    void testGetTemplateGroupByIdReturnsTemplateGroup(){
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

        Schedule schedule = new Schedule();
        schedule.setPriority(1);

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
        templateGroup.setSchedule(schedule);
        when(repository.findById(1L)).thenReturn(Optional.of(templateGroup));

        TemplateGroup retrievedTemplateGroup = service.getGroupById(1L);

        assertThat(retrievedTemplateGroup.getGroup()).isEqualTo(group);
    }

    @Test
    void testGetTemplateGroupByGroupID(){
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
        templateGroup.setSchedule(schedule);
        when(repository.findByGroupId(group.getId())).thenReturn(templateGroup);

        TemplateGroup get_template = service.getTemplateGroupByGroupID(group.getId());

        assertThat(get_template.getGroup()).isEqualTo(group);
    }


    @Test
    @Disabled("this is not working because as the service was made is not possible to mock the mqtt service")
    void testSaveTemplateGroup() throws IOException {

        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(7);
        schedule1.setEndDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartTime(LocalTime.parse("08:30"));
        schedule1.setEndTime(LocalTime.parse("18:30"));
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
        widget.setName("Media");
        widget.setId(1L);
        widget.setContents(List.of(content));
        widget.setPath("static/widgets/media.widget");

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

        when(templateService.getTemplateById(templateGroup.getTemplate().getId())).thenReturn(template);
        when(groupService.getGroupById(templateGroup.getGroup().getId())).thenReturn(group);
        when(repository.save(templateGroup)).thenReturn(templateGroup);
        when(scheduleService.saveSchedule(schedule1)).thenReturn(schedule1);
        when(templateWidgetService.getTemplateWidgetById(1L)).thenReturn(templateWidget);
        when(mqttConfig.getInstance()).thenReturn(ImqttClient);

        when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("test");

        TemplateGroup saved_template = service.saveGroup(templateGroup);

        assertThat(saved_template).isEqualTo(templateGroup);
    }

    @Test
    void testDeleteTemplateGroup(){
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
        templateGroup.setSchedule(schedule);

        service.deleteGroup(1L);
        assertFalse(repository.existsById(1L));
    }

    @Test
    @Disabled("this is not working because as the service was made is not possible to mock the mqtt service")
    void testUpdateTemplateGroup(){
        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(7);
        schedule1.setEndDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartTime(LocalTime.parse("08:30"));
        schedule1.setEndTime(LocalTime.parse("18:30"));
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
        widget.setName("Media");
        widget.setId(1L);
        widget.setContents(List.of(content));
        widget.setPath("static/widgets/media.widget");

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
        when(repository.save(templateGroup)).thenReturn(updated_templateGroup);
        when(templateService.getTemplateById(templateGroup.getTemplate().getId())).thenReturn(template);
        when(templateWidgetService.getTemplateWidgetById(1L)).thenReturn(templateWidget);
        when(scheduleService.saveSchedule(schedule1)).thenReturn(schedule1);
        when(groupService.getGroupById(templateGroup.getGroup().getId())).thenReturn(group);
        when(templateGroupService.sendTemplateGroupToMonitorGroup(Mockito.any(TemplateGroup.class), Mockito.any(MonitorsGroup.class), Mockito.any(Long.class))).thenReturn(templateGroup);
        TemplateGroup updated_template = service.updateTemplateGroup(1L, updated_templateGroup);

        assertThat(updated_template).isEqualTo(updated_templateGroup);

    }

    @Test
    void testGetAllTemplateGroups(){
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
        templateGroup.setSchedule(schedule);

        when(repository.findAll()).thenReturn(List.of(templateGroup));

        Iterable<TemplateGroup> all_templateGroups = service.getAllGroups();

        assertThat(all_templateGroups).contains(templateGroup);

    }

    @Test
    void testIsWidgetContentMedia() {
        Content mediaContent = new Content();
        mediaContent.setType("media");

        Widget widget = new Widget();
        widget.setContents(List.of(mediaContent));

        TemplateWidget templateWidget = new TemplateWidget();
        templateWidget.setWidget(widget);

        Template template = new Template();
        template.setTemplateWidgets(List.of(templateWidget));

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setTemplate(template);
        templateGroup.setGroup(new MonitorsGroup());
        templateGroup.setSchedule(new Schedule());


        service.isWidgetContentMedia(templateWidget);

        assertThat(service.isWidgetContentMedia(templateWidget)).isTrue();

    }

}
