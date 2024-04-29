package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Models.TemplateWidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import deti.uas.uasmartsignage.Configuration.MqttConfig;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import deti.uas.uasmartsignage.Mqtt.TemplateMessage;



@Service
public class TemplateGroupService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TemplateService templateService;
    private final MonitorGroupService monitorGroupService;
    private final TemplateGroupRepository templateGroupRepository;
    private final ContentService contentService;

    public TemplateGroupService(TemplateService templateService, MonitorGroupService monitorGroupService, TemplateGroupRepository templateGroupRepository, ContentService contentService) {
        this.templateService = templateService;
        this.monitorGroupService = monitorGroupService;
        this.templateGroupRepository = templateGroupRepository;
        this.contentService = contentService;
    }



    private final Logger logger = LoggerFactory.getLogger(TemplateGroupService.class);

    public TemplateGroup getGroupById(Long id) {
        return templateGroupRepository.findById(id).orElse(null);
    }

    public TemplateGroup getTemplateGroupByGroupID(Long groupID) {
        return templateGroupRepository.findByGroupId(groupID);
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
            confirmMessage.setFiles(files);
            
            for (Monitor monitor : monitors) {
                String html = generateHTML(template, templateGroup.getContent(), monitor.getWidth(), monitor.getHeight());
                confirmMessage.setHtml(html);

                String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);
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
        Template template = templateService.getTemplateById(templateGroup.getTemplate().getId());
        if (templateGroupById == null) {
            return null;
        }
        List<Monitor> monitors = templateGroupById.getGroup().getMonitors();
        List<String> files = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : templateGroup.getContent().entrySet()) {
            files.add(entry.getValue());
        }
        if (templateGroupById.getTemplate() != templateGroup.getTemplate()) {
            try {
                TemplateMessage confirmMessage = new TemplateMessage();
                confirmMessage.setMethod("TEMPLATE");
                confirmMessage.setFiles(files);

                for (Monitor monitor : monitors) {
                    String html = generateHTML(template, templateGroup.getContent(), monitor.getWidth(), monitor.getHeight());
                    confirmMessage.setHtml(html);

                    String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);
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

    /**
     * Takes a template and the contents to place in the widgets and returns the full html
     * 
     * @param template The Template containing the list of widgets
     * @param contents A Map that contains the ids of Contents and the respective variables to fill in the widgets
     * @param monitorWidth The screen width to be considered when calculating widget sizes
     * @param monitorHeight The screen height to be considered when calculating widget sizes
     * @return The created HTML in a String, or {@code null} if creation fails.
     */
    public String generateHTML(Template template, Map<Integer, String> contents, int monitorWidth, int monitorHeight) {

        List<TemplateWidget> widgets = template.getTemplateWidgets();
        String filePath = "static/base.html";

        try {
            
            // get a base html to add the widgets to
            File baseFile = ResourceUtils.getFile("classpath:" + filePath);
            Document doc = Jsoup.parse(baseFile, "UTF-8");

            for (TemplateWidget widget : widgets) {
                // fill in the variables inside the widgets
                // using the values inside "contents"
                String widgetHTML = loadWidget(widget, contents, monitorWidth, monitorHeight);
                
                if(widgetHTML == null){
                    continue;
                }   

                // add the html of the widget to the main body
                List<Element> widgetElements = Jsoup.parseBodyFragment(widgetHTML).body().children();
                for(Element child : widgetElements)
                    doc.body().appendChild(child);
            }

            logger.info(String.format("HTML for template %s was generated successfully", template.getName()));
            return doc.html();

        } catch (FileNotFoundException e) {
            logger.error(String.format("Could not find file 'resources/%s'", filePath));
            return null;
        } catch (IOException e) {
            logger.error(String.format("Jsoup could not parse the file 'resources/%s'", filePath));
            return null;
        }

    }

    /**
     * Takes a widget and fills in the [[spaces]] with the Contents
     * 
     * @param widget The Widget that we want to complete
     * @param contents A Map that contains the ids of Contents and the respective variables to fill in the widgets
     * @param monitorWidth The screen width to be considered when calculating widget sizes
     * @param monitorHeight The screen height to be considered when calculating widget sizes
     * @return The created HTML in a String, or {@code null} if creation fails.
     */
    private String loadWidget(TemplateWidget widget, Map<Integer, String> contents, int monitorWidth, int monitorHeight) {

        try {
            File widgetFile = ResourceUtils.getFile("classpath:" + widget.getWidget().getPath());
            String widgetHTML = new String(Files.readAllBytes(Paths.get(widgetFile.toURI())));

            widgetHTML = widgetHTML
                    .replace("[[top]]", String.valueOf(monitorHeight * widget.getTop() / 100))
                    .replace("[[left]]", String.valueOf(monitorWidth * widget.getLeftPosition() / 100))
                    .replace("[[width]]", String.valueOf(monitorWidth * widget.getWidth() / 100))
                    .replace("[[height]]", String.valueOf(monitorHeight * widget.getHeight() / 100));

            // go through each of the contents
            // and fill in the variables
            for(int contentID : contents.keySet()){
                String value = contents.get(contentID);
                Content content = contentService.getContentById((long)contentID);
                widgetHTML = widgetHTML.replace("[[" + content.getName() + "]]", value);
            }

            logger.info(String.format("HTML for widget %s was generated successfully", widget.getWidget().getName()));
            return widgetHTML;

        } catch (FileNotFoundException e) {
            logger.error(String.format("Could not find file 'resources/%s'", widget.getWidget().getPath()));
            return null;
        } catch (IOException e) {
            logger.error(String.format("Could not read file 'resources/%s'", widget.getWidget().getPath()));
            return null;
        }

    }

}
