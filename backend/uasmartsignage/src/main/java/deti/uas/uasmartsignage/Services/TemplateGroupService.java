package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Models.TemplateWidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import deti.uas.uasmartsignage.Configuration.MqttConfig;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import deti.uas.uasmartsignage.Mqtt.TemplateMessage;

@Service
public class TemplateGroupService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TemplateService templateService;
    private final MonitorGroupService monitorGroupService;
    private final FileService fileService;
    private final ScheduleService scheduleService;
    private final TemplateGroupRepository templateGroupRepository;
    private final TemplateWidgetService templateWidgetService;

    public TemplateGroupService(TemplateWidgetService templateWidgetService, TemplateService templateService, MonitorGroupService monitorGroupService, FileService fileService, ScheduleService scheduleService,TemplateGroupRepository templateGroupRepository) {
        this.templateWidgetService = templateWidgetService;
        this.templateService = templateService;
        this.monitorGroupService = monitorGroupService;
        this.fileService = fileService;
        this.scheduleService = scheduleService;
        this.templateGroupRepository = templateGroupRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(TemplateGroupRepository.class);

    /**
     * Returns a TemplateGroup with the given id
     * 
     * @param id The id of the TemplateGroup to return
     * @return The TemplateGroup with the given id, or {@code null} if it does not exist
     */
    public TemplateGroup getGroupById(Long id) {
        return templateGroupRepository.findById(id).orElse(null);
    }

    /**
     * Returns a TemplateGroup with the given groupID
     * 
     * @param groupID The groupID of the TemplateGroup to return
     * @return The TemplateGroup with the given groupID, or {@code null} if it does not exist
     */
    public TemplateGroup getTemplateGroupByGroupID(Long groupID) {
        return templateGroupRepository.findByGroupId(groupID);
    }

    /**
     * Processes the content of a TemplateGroup, replacing the ids of the contents with the actual file names
     * 
     * @param content The content of the TemplateGroup
     * @return A Map containing the updated content and a list of download files
     */
    public Map<String, Object> processTemplateGroupContent(Map<Integer, String> content) {
        Map<Integer, String> updatedContent = new HashMap<>();
        List<String> downloadFiles = new ArrayList<>();
        if (content != null) {
            for (Map.Entry<Integer, String> entry : content.entrySet()) {
                TemplateWidget widget = templateWidgetService.getTemplateWidgetById((long) entry.getKey()); 
                if (!isWidgetContentMedia(widget)) {
                    continue;
                }
    
                Optional<CustomFile> file = fileService.getFileOrDirectoryById(Long.parseLong(entry.getValue()));
                if (file.isEmpty()) {
                    continue;
                }
    
                if ("directory".equals(file.get().getType())) {
                    List<CustomFile> files = file.get().getSubDirectories();
                    if (files != null) {
                        StringBuilder dirFiles = new StringBuilder();
                        for (CustomFile f : files) {
                            if (!"directory".equals(f.getType())) {
                                downloadFiles.add("http://localhost:8080/api/files/download/" + f.getId());
                                dirFiles.append(f.getName()).append("\",\"");
                            }
                        }
                        entry.setValue(dirFiles.substring(0, dirFiles.length() - 3));
                    }
                } else {
                    downloadFiles.add("http://localhost:8080/api/files/download/" + entry.getValue());
                    entry.setValue(file.get().getName());
                }
                updatedContent.put(entry.getKey(), entry.getValue());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("updatedContent", updatedContent);
        result.put("downloadFiles", downloadFiles);
        return result;
    }

    /**
     * Sends a TemplateGroup to a MonitorsGroup
     * 
     * @param templateGroup The TemplateGroup to send
     * @param monitorGroup The MonitorsGroup to send the TemplateGroup to
     * @return The TemplateGroup that was sent
     */
    public TemplateGroup sendTemplateGroupToMonitorGroup(TemplateGroup templateGroup, MonitorsGroup monitorGroup) {
        Template template = templateService.getTemplateById(templateGroup.getTemplate().getId());
        Schedule schedule;
        if (templateGroup.getSchedule().getId() == null) {
            schedule = scheduleService.saveSchedule(templateGroup.getSchedule());
        }
        else {
            schedule = scheduleService.getScheduleById(templateGroup.getSchedule().getId());
        }
        List<String> downloadFiles = new ArrayList<>();
        if (templateGroup.getContent() != null) {
            Map<String, Object> contentResult = processTemplateGroupContent(templateGroup.getContent());
            Map<Integer, String> updatedContent = (Map<Integer, String>) contentResult.get("updatedContent");
            templateGroup.setContent(updatedContent);
            downloadFiles = (List<String>) contentResult.get("downloadFiles");
        }
        try {
            TemplateMessage confirmMessage = new TemplateMessage();
            confirmMessage.setMethod("TEMPLATE");
            confirmMessage.setFiles(downloadFiles);
            confirmMessage.setSchedule(schedule.toMqttFormat());
            List<Monitor> monitors = monitorGroup.getMonitors();
            for (Monitor monitor : monitors) {
                String html = generateHTML(template, templateGroup.getContent(), monitor.getWidth(), monitor.getHeight());
                confirmMessage.setHtml(html);
                String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);
                MqttConfig.getInstance().publish(monitor.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
            }

            
        } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
            e.printStackTrace();
    
        }

        templateGroup.setTemplate(template);
        templateGroup.setGroup(monitorGroup);
        templateGroup.setSchedule(schedule);
        templateGroupRepository.save(templateGroup);
        return templateGroup;
    }
    
    

    /**
     * Saves a TemplateGroup
     * 
     * @param templateGroup The TemplateGroup to save
     * @return The saved TemplateGroup
     */
    public TemplateGroup saveGroup(TemplateGroup templateGroup) {
        MonitorsGroup monitorGroup = monitorGroupService.getGroupById(templateGroup.getGroup().getId());
        return sendTemplateGroupToMonitorGroup(templateGroup, monitorGroup);
    }

    /**
     * Deletes a TemplateGroup with the given id
     * 
     * @param id The id of the TemplateGroup to delete
     */
    public void deleteGroup(Long id) {
        templateGroupRepository.deleteById(id);
    }

    /**
     * Returns all TemplateGroups
     * 
     * @return An Iterable of all TemplateGroups
     */
    public Iterable<TemplateGroup> getAllGroups() {
        return templateGroupRepository.findAll();
    }


    /**
     * Updates a TemplateGroup with the given id
     * 
     * @param id The id of the TemplateGroup to update
     * @param templateGroup The new TemplateGroup to replace the old one
     * @return The updated TemplateGroup, or {@code null} if the id does not exist
     */
    public TemplateGroup updateTemplateGroup(Long id, TemplateGroup templateGroup) {
        TemplateGroup templateGroupById = templateGroupRepository.findById(id).orElse(null);
        if (templateGroupById != templateGroup){
            MonitorsGroup monitorGroup = templateGroupById.getGroup();
            return sendTemplateGroupToMonitorGroup(templateGroup, monitorGroup);
        }
        return null;
    }

    
    private boolean isWidgetContentMedia(TemplateWidget widget){
        return widget.getWidget().getContents().get(0).getType().equals("media");
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
            String pathToBaseFile = filePath;
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream(pathToBaseFile);
            Document doc = Jsoup.parse(inputStream, "UTF-8", pathToBaseFile);


            for (TemplateWidget widget : widgets) {
                // fill in the variables inside the widgets
                // using the values inside "contents"

               String widgetHTML = loadWidget(widget, contents.get(Math.toIntExact(widget.getId())), monitorWidth, monitorHeight);
                
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
    private String loadWidget(TemplateWidget widget, String value, int monitorWidth, int monitorHeight) {

        try {
            String pathToWidget = widget.getWidget().getPath();
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream(pathToWidget);
            String widgetHTML = new String(inputStream.readAllBytes(), "UTF-8");

            widgetHTML = widgetHTML
                    .replace("[[top]]", String.valueOf(monitorHeight * widget.getTop() / 100))
                    .replace("[[left]]", String.valueOf(monitorWidth * widget.getLeftPosition() / 100))
                    .replace("[[width]]", String.valueOf(monitorWidth * widget.getWidth() / 100))
                    .replace("[[height]]", String.valueOf(monitorHeight * widget.getHeight() / 100));
            
            if(value != null){
                Content content = widget.getWidget().getContents().get(0);
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
