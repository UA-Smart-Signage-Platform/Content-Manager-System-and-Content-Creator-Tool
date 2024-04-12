package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Models.TemplateWidget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import deti.uas.uasmartsignage.Configuration.MqttConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import deti.uas.uasmartsignage.Mqtt.TemplateMessage;

import deti.uas.uasmartsignage.Services.TemplateService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;


@Service
public class TemplateGroupService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TemplateService templateService;
    private final MonitorGroupService monitorGroupService;

    public TemplateGroupService(TemplateService templateService, MonitorGroupService monitorGroupService) {
        this.templateService = templateService;
        this.monitorGroupService = monitorGroupService;
    }

    @Autowired
    private TemplateGroupRepository templateGroupRepository;

    public TemplateGroup getGroupById(Long id) {
        return templateGroupRepository.findById(id).orElse(null);
    }

    public TemplateGroup saveGroup(TemplateGroup templateGroup) {
        Template template = templateService.getTemplateById(templateGroup.getTemplate().getId());
        MonitorsGroup monitorGroup = monitorGroupService.getGroupById(templateGroup.getGroup().getId());
        List<Monitor> monitors = monitorGroup.getMonitors();
        templateGroup.setTemplate(template);
        templateGroup.setGroup(monitorGroup);
        List<String> files = new ArrayList<>();
        if (templateGroup.getContent() != null) {
            for (Map.Entry<Integer, String> entry : templateGroup.getContent().entrySet()) {
                files.add(entry.getValue());
            }
        }
        
        try {
            TemplateMessage confirmMessage = new TemplateMessage();
            confirmMessage.setMethod("TEMPLATE");
            //confirmMessage.setHtml(templateGroup.getTemplate().getPath());
            confirmMessage.setFiles(files);
            

            String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);
            for (Monitor monitor : monitors) {
                MqttConfig.getInstance().publish(monitor.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
            }

            
        } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
            e.printStackTrace();
        }
        
        return templateGroupRepository.save(templateGroup);
    }

    public void deleteGroup(Long id) {
        templateGroupRepository.deleteById(id);
    }

    public Iterable<TemplateGroup> getAllGroups() {
        return templateGroupRepository.findAll();
    }

    public TemplateGroup updateTemplateGroup(Long id, TemplateGroup templateGroup) {
        TemplateGroup templateGroupById = templateGroupRepository.findById(id).orElse(null);
        if (templateGroupById == null) {
            return null;
        }
        TemplateGroup templateGroupAfter = templateGroupRepository.findById(templateGroup.getId()).orElse(null);
        List<Monitor> monitors = templateGroupById.getGroup().getMonitors();
        List<String> files = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : templateGroupAfter.getContent().entrySet()) {
            files.add(entry.getValue());
        }
        if (templateGroupById.getTemplate() != templateGroup.getTemplate()) {
            try {
                TemplateMessage confirmMessage = new TemplateMessage();
                confirmMessage.setMethod("TEMPLATE");
                //confirmMessage.setHtml(templateGroupAfter.getTemplate().getPath());
                confirmMessage.setFiles(files);
                
    
                String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);
                System.out.println("Sending confirm message: " + confirmMessageJson);

                for (Monitor monitor : monitors) {
                    MqttConfig.getInstance().publish(monitor.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
                }

            } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
                // Handle exception
                e.printStackTrace();
            }
        }

        

        templateGroupById.setTemplate(templateGroup.getTemplate());
        templateGroupById.setTemplate(templateGroup.getTemplate());
        templateGroupById.setContent(templateGroup.getContent());
        return templateGroupRepository.save(templateGroupById);
    }

    
    
}
