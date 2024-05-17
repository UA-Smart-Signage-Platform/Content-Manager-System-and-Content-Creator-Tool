package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import deti.uas.uasmartsignage.Configuration.MqttConfig;
import deti.uas.uasmartsignage.Mqtt.TemplateMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import deti.uas.uasmartsignage.Models.*;
import deti.uas.uasmartsignage.Mqtt.MqttSubscriberService;
import deti.uas.uasmartsignage.Services.ContentService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import deti.uas.uasmartsignage.Services.TemplateService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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

    @Mock
    private IMqttClient mqttClient;

    @Mock
    private MqttConfig mqttConfig;

    @Mock
    private MqttMessage mqttMessage;

    @Mock
    private TemplateMessage templateMessage;

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
        when(repository.findById(1L)).thenReturn(Optional.of(templateGroup));

        TemplateGroup retrievedTemplateGroup = service.getGroupById(1L);

        assertThat(retrievedTemplateGroup.getGroup()).isEqualTo(group);
    }

    @Test
    void testGetTemplateGroupByGroupID(){
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
        when(repository.findByGroupId(group.getId())).thenReturn(templateGroup);

        TemplateGroup get_template = service.getTemplateGroupByGroupID(group.getId());

        assertThat(get_template.getGroup()).isEqualTo(group);
    }


    @Test
    void testSaveTemplateGroup() throws MqttException, JsonProcessingException {

        //MockitoAnnotations.openMocks(this);

        // Mock MqttConfig.getInstance() to return the mock mqttClient
        //when(mqttConfig.getInstance()).thenReturn(mqttClient);

        // Mock any necessary behavior of mqttClient
        //when(mqttClient.isConnected()).thenReturn(true);


        //when(templateMessage.getMethod()).thenReturn("TEMPLATE");
        //doNothing().when(mqttClient).publish(anyString(), any(MqttMessage.class));



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

        

        when(templateService.getTemplateById(templateGroup.getTemplate().getId())).thenReturn(template);
        when(groupService.getGroupById(templateGroup.getGroup().getId())).thenReturn(group);
        when(repository.save(templateGroup)).thenReturn(templateGroup);

        TemplateGroup saved_template = service.saveGroup(templateGroup);

        assertThat(saved_template).isEqualTo(templateGroup);
    }

    @Test
    void testDeleteTemplateGroup(){
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

        service.deleteGroup(1L);
        assertFalse(repository.existsById(1L));

        //verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateTemplateGroup(){
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
        templateGroup.setContent(Map.of(1,"Content1"));

        when(repository.findById(1L)).thenReturn(Optional.of(templateGroup));
        when(repository.save(templateGroup)).thenReturn(templateGroup);

        TemplateGroup updated_template = service.updateTemplateGroup(1L, templateGroup);

        assertThat(updated_template).isEqualTo(templateGroup);
    }
}
