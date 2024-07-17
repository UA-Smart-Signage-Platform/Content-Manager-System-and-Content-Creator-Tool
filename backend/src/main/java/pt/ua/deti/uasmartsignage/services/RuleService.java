package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.models.Rule;
import pt.ua.deti.uasmartsignage.models.Template;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import pt.ua.deti.uasmartsignage.repositories.RuleRepository;
import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.dto.RuleDTO;
import pt.ua.deti.uasmartsignage.enums.Severity;

@Service
@RequiredArgsConstructor
public class RuleService {

    // @Value("${ip.address}")
    // private String serverAddress;

    private final RuleRepository ruleRepository;
    private final TemplateService templateService;
    private final MonitorGroupService monitorGroupService;
    private final LogsService logsService;

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public Rule getRuleById(String id){
        return ruleRepository.findById(id).orElse(null);
    }

    public Rule saveRule(RuleDTO ruleDTO) {
        Rule rule = convertDTOToRule(ruleDTO);
        if (rule == null) return null;
        rule.setId(null);
        return ruleRepository.save(rule);
    }

    public void deleteRuleById(String id) {
        ruleRepository.deleteById(id);
    }

    public Rule updateRule(String id, RuleDTO ruleDTO) {
        Rule rule = convertDTOToRule(ruleDTO);
        if (rule == null) return null;
        rule.setId(id);
        return ruleRepository.save(rule);
    }

    public Rule convertDTOToRule(RuleDTO ruleDTO) {

        // check if group exists
        long groupId = ruleDTO.getGroupId();
        if (monitorGroupService.getGroupById(groupId) == null){
            return null;
        }
        
        // check if template exists
        Template template = templateService.getTemplateById(ruleDTO.getTemplateId());
        if (template == null){
            return null;
        }

        // create the rule
        Rule rule = Rule.builder()
                        .groupId(groupId)
                        .template(template)
                        .schedule(ruleDTO.getSchedule())
                        .build();

        // add the chosen values
        Map<Long, Map<String, Object>> chosenValues = ruleDTO.getChosenValues();
        if (chosenValues != null){
            try{        
                for (Long templateWidgetId : chosenValues.keySet()){
                    Map<String, Object> widgetChosenValues = chosenValues.get(templateWidgetId);
                    for (String key : widgetChosenValues.keySet()){
                        Object value = widgetChosenValues.get(key);
                        rule.setChosenValue(templateWidgetId, key, value);
                    }
                }
            }
            catch(IllegalArgumentException exception){
                logsService.addBackendLog(Severity.ERROR, 
                                            this.getClass().getSimpleName(),
                                            "convertDTOToRule", 
                                            exception.getMessage());
                return null;
            }
        }
        
        return rule;
    }

    // /**
    //  * Returns a TemplateGroup with the given id
    //  * 
    //  * @param id The id of the TemplateGroup to return
    //  * @return The TemplateGroup with the given id, or {@code null} if it does not exist
    //  */
    // public TemplateGroup getGroupById(Long id) {
    //     logger.info("Getting TemplateGroup by {} ", templateGroupRepository.findById(id));
    //     return templateGroupRepository.findById(id).orElse(null);
    // }

    // /**
    //  * Returns a TemplateGroup with the given groupID
    //  * 
    //  * @param groupID The groupID of the TemplateGroup to return
    //  * @return The TemplateGroup with the given groupID, or {@code null} if it does not exist
    //  */
    // public TemplateGroup getTemplateGroupByGroupID(Long groupID) {
    //     return templateGroupRepository.findByGroupId(groupID);
    // }

    // /**
    //  * Processes the content of a TemplateGroup, replacing the ids of the contents with the actual file names
    //  * 
    //  * @param content The content of the TemplateGroup
    //  * @return A Map containing the updated content and a list of download files
    //  */
    // public Map<String, Object> processTemplateGroupContent(Map<Integer, String> content) {
    //     Map<Integer, String> updatedContent = new HashMap<>();
    //     List<String> downloadFiles = new ArrayList<>();
    //     if (content == null) {
    //         return Collections.emptyMap();
    //     }
    //     for (Map.Entry<Integer, String> entry : content.entrySet()) {
    //         processEntry(entry, updatedContent, downloadFiles);
    //     }
    //     return buildResultMap(updatedContent, downloadFiles);
    // }
    
    // /**
    //  * Processes a single entry of the content of a TemplateGroup
    //  * 
    //  * @param entry The entry to process
    //  * @param updatedContent The Map to store the updated content
    //  * @param downloadFiles The List to store the download files
    //  */
    // private void processEntry(Map.Entry<Integer, String> entry, Map<Integer, String> updatedContent, List<String> downloadFiles) {
    //     TemplateWidget widget = templateWidgetService.getTemplateWidgetById((long) entry.getKey());
    //     if (isWidgetContentMedia(widget)) {
    //         Optional<CustomFile> file = fileService.getFileOrDirectoryById(Long.parseLong(entry.getValue()));
    //         file.ifPresent(customFile -> {
    //             if ("directory".equals(customFile.getType())) {
    //                 processDirectory(customFile, entry, downloadFiles, updatedContent);
    //             } else {
    //                 processFile(customFile, entry, updatedContent, downloadFiles);
    //             }
    //         });
    //     }
    //     else{
    //         // If the widget is not a media widget, just add the content to the updatedContent
    //         updatedContent.put(entry.getKey(), entry.getValue());
    //     }
    // }
    
    // /**
    //  * Processes a directory
    //  * 
    //  * @param directory The directory to process
    //  * @param entry The entry to process
    //  * @param downloadFiles The List to store the download files
    //  */
    // private void processDirectory(CustomFile directory, Map.Entry<Integer, String> entry, List<String> downloadFiles, Map<Integer, String> updatedContent) {
    //     // Add all the files in the directory to the downloadFiles and update the content with the file names
    //     List<CustomFile> files = directory.getSubDirectories();
    //     if (files != null) {
    //         StringBuilder dirFiles = new StringBuilder();
    //         for (CustomFile f : files) {
    //             if (!"directory".equals(f.getType())) {
    //                 downloadFiles.add(serverAddress + "/api/files/download/" + f.getId());
    //                 dirFiles.append(f.getName()).append("\",\"");
    //             }
    //         }
    //         updatedContent.put(entry.getKey(), dirFiles.substring(0, dirFiles.length() - 3));
    //     }
    // }
    
    // /**
    //  * Processes a file
    //  * 
    //  * @param file The file to process
    //  * @param entry The entry to process
    //  * @param updatedContent The Map to store the updated content
    //  * @param downloadFiles The List to store the download files
    //  */
    // private void processFile(CustomFile file, Map.Entry<Integer, String> entry, Map<Integer, String> updatedContent, List<String> downloadFiles) {
    //     // Add the file to the downloadFiles and update the content with the file name
    //     downloadFiles.add(serverAddress + "/api/files/download/" + entry.getValue());
    //     entry.setValue(file.getName());
    //     updatedContent.put(entry.getKey(), entry.getValue());
    // }
    
    // /**
    //  * Builds a Map with the updated content and the download files
    //  * 
    //  * @param updatedContent The updated content
    //  * @param downloadFiles The download files
    //  * @return The built Map
    //  */
    // private Map<String, Object> buildResultMap(Map<Integer, String> updatedContent, List<String> downloadFiles) {
    //     // Return the updated content and the download files
    //     Map<String, Object> result = new HashMap<>();
    //     result.put("updatedContent", updatedContent);
    //     result.put(DOWNLOAD_FILES, downloadFiles);
    //     return result;
    // }

    // /**
    //  * Sends all schedules to a list of Monitors
    //  * 
    //  * @param monitors The list of Monitors to send the schedules to
    //  */
    // public void sendAllSchedulesToMonitorGroup(List<Monitor> monitors) {

    //     if (monitors.isEmpty()) {
    //         return;
    //     }
    //     // Get the last monitor to get the monitorGroup (the one just added)
    //     Monitor tempMonitor = monitors.get(monitors.size() - 1);
    //     MonitorsGroup monitorGroup = monitorGroupService.getGroupById(tempMonitor.getGroup().getId());
    //     List <TemplateGroup> templateGroups = monitorGroup.getTemplateGroups();

    //     // Get all the rules for the monitorGroup
    //     List <Map<String, Object>> rules = new ArrayList<>();
    //     for (TemplateGroup group : templateGroups) {
    //         Map<String, Object> rule = new HashMap<>();
    //         Template template = templateService.getTemplateById(group.getTemplate().getId());
    //         Schedule schedule = group.getSchedule();
    //         List<String> downloadFiles = new ArrayList<>();
    //         if (group.getContent() != null) {
    //             Map<String, Object> contentResult = processTemplateGroupContent(group.getContent());
    //             Map<Integer, String> updatedContent = (Map<Integer, String>) contentResult.get("updatedContent");
    //             group.setContent(updatedContent);
    //             downloadFiles = (List<String>) contentResult.get(DOWNLOAD_FILES);
    //         }
    //         rule.put("template", template);
    //         rule.put(SCHEDULE, schedule.toMqttFormat());
    //         rule.put(DOWNLOAD_FILES, downloadFiles);
    //         rule.put("group", group);
    //         rules.add(rule);
    //     }
        
    //     try{
    //         RulesMessage rulesMessage = new RulesMessage();
    //         rulesMessage.setMethod("RULES");
    //         for (Monitor monitor : monitors) {
    //             List <Map<String, Object>> rulesToSend = new ArrayList<>();
    //             List <String> filesToSend = new ArrayList<>();
                
    //             for (Map<String, Object> rule : rules) {
    //                 Map<String, Object> ruleToSend = new HashMap<>();

    //                 Template template = (Template) rule.get("template");
    //                 TemplateGroup group = (TemplateGroup) rule.get("group");
    //                 // Generate the HTML for the template
    //                 String html = generateHTML(template, group.getContent(), monitor.getWidth(), monitor.getHeight());
                    
    //                 ruleToSend.put("html", html);
    //                 ruleToSend.put(SCHEDULE, rule.get(SCHEDULE));
    //                 rulesToSend.add(ruleToSend);

    //                 // Add the files to the list of files to send (no duplicates)
    //                 for (String file : (List<String>) rule.get(DOWNLOAD_FILES)) {
    //                     if (!filesToSend.contains(file)) {
    //                         filesToSend.add(file);
    //                     }
    //                 }
    //             }
    //             rulesMessage.setRules(rulesToSend);
    //             rulesMessage.setFiles(filesToSend);
                
    //             // Send the rules to the monitor
    //             String rulesMessageJson = objectMapper.writeValueAsString(rulesMessage);
    //             mqttConfig.getInstance().publish(monitor.getUuid(), new MqttMessage(rulesMessageJson.getBytes()));
    //         }
    //     } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
    //         logger.error("Could not send rules to monitors");
    //     }

        
    // }

    // /**
    //  * Sends a TemplateGroup to a MonitorsGroup
    //  * 
    //  * @param templateGroup The TemplateGroup to send
    //  * @param monitorGroup The MonitorsGroup to send the TemplateGroup to
    //  * @param id The id of the TemplateGroup to send
    //  * @return The TemplateGroup that was sent
    //  */
    // public TemplateGroup sendTemplateGroupToMonitorGroup(TemplateGroup templateGroup, MonitorsGroup monitorGroup, Long id) {
    //     TemplateGroup templateGroupById;
    //     boolean isNew = false;

    //     if (id != null){
    //         templateGroupById = templateGroupRepository.findById(id).orElse(null);
    //     } else {
    //         templateGroupById = templateGroup;
    //         isNew = true;
    //     }

    //     if (templateGroupById == null) {
    //         return null;
    //     }
        

    //     Schedule schedule;
    //     if (templateGroup.getSchedule().getId() == null) {
    //         schedule = scheduleService.saveSchedule(templateGroup.getSchedule());
    //     }
    //     else {
    //         schedule = scheduleService.getScheduleById(templateGroup.getSchedule().getId());
    //     }

    //     MonitorsGroup monitorGroupById = monitorGroupService.getGroupById(monitorGroup.getId());
    //     templateGroupById.setTemplate(templateGroup.getTemplate());
    //     templateGroupById.setSchedule(schedule);
    //     templateGroupById.setContent(templateGroup.getContent());
    //     templateGroupRepository.save(templateGroupById);

    //     // Add the templateGroup to the monitorGroup (if it is not a new templateGroup) this is setted in monitorgroup because of the fetch type
    //     List<TemplateGroup> templateGroups = monitorGroupById.getTemplateGroups();
    //     if (!isNew){
    //         templateGroups.remove(templateGroupById);
    //     }
    //     templateGroups.add(templateGroupById);
    //     monitorGroupById.setTemplateGroups(templateGroups);
    //     monitorGroupService.saveGroup(monitorGroupById);

    //     List<Monitor> monitors = monitorGroupById.getMonitors();
    //     sendAllSchedulesToMonitorGroup(monitors);

    //     return templateGroupById;
    // }
    
    

    // /**
    //  * Saves a TemplateGroup
    //  * 
    //  * @param templateGroup The TemplateGroup to save
    //  * @return The saved TemplateGroup
    //  */
    // public TemplateGroup saveGroup(TemplateGroup templateGroup) {
    //     MonitorsGroup monitorGroup = monitorGroupService.getGroupById(templateGroup.getGroup().getId());
    //     return sendTemplateGroupToMonitorGroup(templateGroup, monitorGroup, templateGroup.getId());
    // }

    // /**
    //  * Deletes a TemplateGroup with the given id
    //  * 
    //  * @param id The id of the TemplateGroup to delete
    //  */
    // public void deleteGroup(Long id) {
    //     TemplateGroup templateGroup = templateGroupRepository.findById(id).orElse(null);

    //     if (templateGroup == null) {
    //         return;
    //     }

    //     MonitorsGroup monitorGroup = monitorGroupService.getGroupById(templateGroup.getGroup().getId());
    //     monitorGroup.getTemplateGroups().remove(templateGroup);
    //     monitorGroupService.saveGroup(monitorGroup);
    //     templateGroupRepository.deleteById(id);

    //     List<Monitor> monitors = monitorGroup.getMonitors();
    //     sendAllSchedulesToMonitorGroup(monitors);
    // }


    // /**
    //  * Deletes the templateGrous in a list of TemplateGroupsIds
    //  * 
    //  * @param templateGroups The list of TemplateGroups to delete
    //  */
    // public void deleteGroups(List<Integer> templateGroupsIds) {
    //     if (templateGroupsIds == null) {
    //         return;
    //     }
    //     if (templateGroupsIds.isEmpty()) {
    //         return;
    //     }

    //     // Get the monitorGroup of the first templateGroup (they all belong to the same monitorGroup)
    //     Long templateGroupId = Long.valueOf(templateGroupsIds.get(0));
    //     TemplateGroup tempGroup = templateGroupRepository.findById(templateGroupId).orElse(null);
    //     MonitorsGroup monitorGroup = monitorGroupService.getGroupById(tempGroup.getGroup().getId());

    //     // Delete all the templateGroups updating the monitorGroup first
    //     for (int id : templateGroupsIds) { 
    //         Long templateGroupID = Long.valueOf(id);
    //         TemplateGroup group = templateGroupRepository.findById(templateGroupID).orElse(null);

    //         if (group == null) {
    //             return;
    //         }

    //         monitorGroup.getTemplateGroups().remove(group);
    //         monitorGroupService.saveGroup(monitorGroup);
    //         templateGroupRepository.deleteById(templateGroupID);
    //     }

    //     List<Monitor> monitors = monitorGroup.getMonitors();
    //     sendAllSchedulesToMonitorGroup(monitors);
    // }

    // /**
    //  * Returns all TemplateGroups
    //  * 
    //  * @return An Iterable of all TemplateGroups
    //  */


    // /**
    //  * Updates a TemplateGroup with the given id
    //  * 
    //  * @param id The id of the TemplateGroup to update
    //  * @param templateGroup The new TemplateGroup to replace the old one
    //  * @return The updated TemplateGroup, or {@code null} if the id does not exist
    //  */
    // public TemplateGroup updateTemplateGroup(Long id, TemplateGroup templateGroup) {
    //     TemplateGroup templateGroupById = templateGroupRepository.findById(id).orElse(null);
    //     if (templateGroupById != templateGroup){
    //         MonitorsGroup monitorGroup = monitorGroupService.getGroupById(templateGroup.getGroup().getId());
    //         return sendTemplateGroupToMonitorGroup(templateGroup, monitorGroup, id);
    //     }
    //     return null;
    // }

    
    // public boolean isWidgetContentMedia(TemplateWidget widget){
    //     return widget.getWidget().getContents().get(0).getType().equals("media");
    // }

    // /**
    //  * Takes a template and the contents to place in the widgets and returns the full html
    //  * 
    //  * @param template The Template containing the list of widgets
    //  * @param contents A Map that contains the ids of Contents and the respective variables to fill in the widgets
    //  * @param monitorWidth The screen width to be considered when calculating widget sizes
    //  * @param monitorHeight The screen height to be considered when calculating widget sizes
    //  * @return The created HTML in a String, or {@code null} if creation fails.
    //  */
    // public String generateHTML(Template template, Map<Integer, String> contents, int monitorWidth, int monitorHeight) {
    //     List<TemplateWidget> widgets = template.getTemplateWidgets();
    //     widgets.sort(new TemplateWidget.ZIndexComparator());
    //     String filePath = "static/base.html";

    //     try {
            
    //         // get a base html to add the widgets to
    //         String pathToBaseFile = filePath;
    //         ClassLoader cl = this.getClass().getClassLoader();
    //         InputStream inputStream = cl.getResourceAsStream(pathToBaseFile);
    //         Document doc = Jsoup.parse(inputStream, "UTF-8", pathToBaseFile);


    //         for (TemplateWidget widget : widgets) {
    //             // fill in the variables inside the widgets
    //             // using the values inside "contents"

    //            String widgetHTML = loadWidget(widget, contents.get(Math.toIntExact(widget.getId())), monitorWidth, monitorHeight);
                
    //             if(widgetHTML == null){
    //                 continue;
    //             }   

    //             // add the html of the widget to the main body
    //             List<Element> widgetElements = Jsoup.parseBodyFragment(widgetHTML).body().children();
    //             for(Element child : widgetElements)
    //                 doc.body().appendChild(child);
    //         }

    //         logger.info(String.format("HTML for template %s was generated successfully", template.getName()));
    //         return doc.html();

    //     } catch (FileNotFoundException e) {
    //         logger.error(String.format("Could not find file 'resources/%s'", filePath));
    //         return null;
    //     } catch (IOException e) {
    //         logger.error(String.format("Jsoup could not parse the file 'resources/%s'", filePath));
    //         return null;
    //     }

    // }

    // /**
    //  * Takes a widget and fills in the [[spaces]] with the Contents
    //  * 
    //  * @param widget The Widget that we want to complete
    //  * @param contents A Map that contains the ids of Contents and the respective variables to fill in the widgets
    //  * @param monitorWidth The screen width to be considered when calculating widget sizes
    //  * @param monitorHeight The screen height to be considered when calculating widget sizes
    //  * @return The created HTML in a String, or {@code null} if creation fails.
    //  */
    // private String loadWidget(TemplateWidget widget, String value, int monitorWidth, int monitorHeight) {

    //     try {
    //         String pathToWidget = widget.getWidget().getPath();
    //         ClassLoader cl = this.getClass().getClassLoader();
    //         InputStream inputStream = cl.getResourceAsStream(pathToWidget);
    //         String widgetHTML = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    //         String widgetID = UUID.randomUUID().toString().replace("-", "");

    //         widgetHTML = widgetHTML
    //                 .replace("[[top]]", String.valueOf(monitorHeight * widget.getTop() / 100))
    //                 .replace("[[left]]", String.valueOf(monitorWidth * widget.getLeftPosition() / 100))
    //                 .replace("[[width]]", String.valueOf(monitorWidth * widget.getWidth() / 100))
    //                 .replace("[[height]]", String.valueOf(monitorHeight * widget.getHeight() / 100))
    //                 .replace("[[widgetID]]", widgetID);
            
    //         if(value != null){
    //             Content content = widget.getWidget().getContents().get(0);
    //             widgetHTML = widgetHTML.replace("[[" + content.getName() + "]]", value);    
    //         }
            
    //         logger.info(String.format("HTML for widget %s was generated successfully", widget.getWidget().getName()));
    //         return widgetHTML;

    //     } catch (FileNotFoundException e) {
    //         logger.error(String.format("Could not find file 'resources/%s'", widget.getWidget().getPath()));
    //         return null;
    //     } catch (IOException e) {
    //         logger.error(String.format("Could not read file 'resources/%s'", widget.getWidget().getPath()));
    //         return null;
    //     }

    // }

}
