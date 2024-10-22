package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.Rule;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.models.Widget;
import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;
import pt.ua.deti.uasmartsignage.models.embedded.WidgetVariable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import pt.ua.deti.uasmartsignage.repositories.RuleRepository;
import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.dto.RuleDTO;
import pt.ua.deti.uasmartsignage.enums.Log;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.enums.WidgetVariableType;
import pt.ua.deti.uasmartsignage.events.RulesChangedEvent;

@Service
@RequiredArgsConstructor
public class RuleService {

    // @Value("${ip.address}")
    // private String serverAddress;

    private final RuleRepository ruleRepository;
    private final TemplateService templateService;
    private final MonitorGroupService monitorGroupService;
    private final LogsService logsService;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;

    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);
    private final String source = this.getClass().getSimpleName();

    /**
     * Retrieves all existent rules.
     *
     * @return All rules.
    */
    public List<Rule> getAllRules() {
        String operation = "getAllRules";
        String description = "Retrieved all rules";

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        return ruleRepository.findAll();
    }

    /**
     * Retrieves and returns all rules based on group ID.
     *
     * @param groupId The ID of the group to retrieve all rules from.
     * @return The rules that belong to group with the specified ID.
    */
    public List<Rule> getAllRulesForGroup(Long groupId) {
        String operation = "getAllRulesForGroup";
        String description = "Retrieved all rules based on group ID: " + groupId;

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        return ruleRepository.findByGroupId(groupId);
    }

    /**
     * Retrieves rule based on given ID.
     *
     * @param id The ID of the rule.
     * @return The rule based on the ID or empty if not found.
    */
    public Optional<Rule> getRuleById(String id){
        logger.info("Retrieving rule with ID: {}", id);
        Optional<Rule> rule = ruleRepository.findById(id);

        String operation = "getRuleById";
        String description = "Retrieved rule with ID: " + id;

        if (rule.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return Optional.empty();
        }

        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return rule;
    }

    /**
     * Saves rule based on Data Transfer Object (DTO).
     * 
     * @param ruleDTO The DTO with information necessary to create a rule.
     * @return The newly created Rule, or null if no rule was created.
    */
    public Rule saveRule(RuleDTO ruleDTO) {
        Rule rule = convertDTOToRule(ruleDTO);

        String operation = "saveRule(DTO)";
        String description = "Saved rule for groupId: " + ruleDTO.getGroupId();

        if (rule == null){
            description = "Failed to create rule for groupId: " + ruleDTO.getGroupId();
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        } 

        rule.setId(null);
       
        Rule savedRule = ruleRepository.save(rule);

        eventPublisher.publishEvent(new RulesChangedEvent(this, savedRule.getGroupId()));
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return savedRule;
    }

    /**
     * Deletes rule based on given ID.
     * 
     * @param id The ID of the desired rule to delete.
     * @return Boolean with true if rule was deleted or false if it failed.
    */
    public boolean deleteRuleById(String id) {
        Optional<Rule> rule = getRuleById(id);

        String operation = "deleteRuleById";
        String description = "Deleted rule with ID: " + id;

        if (rule.isEmpty()){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return false;
        }

        ruleRepository.deleteById(id);
        eventPublisher.publishEvent(new RulesChangedEvent(this, rule.get().getGroupId()));
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return true;
    }

    /**
     * Updates rule based on ID and Data Transfer Object (DTO).
     * 
     * @param id The ID of the desired rule to update.
     * @param ruleDTO The DTO with information necessary to update the rule.
     * @return The updated rule.
    */
    public Rule updateRule(String id, RuleDTO ruleDTO) {
        Rule rule = convertDTOToRule(ruleDTO);

        String operation = "updateRule(DTO)";
        String description = "Updated rule with ID: " + id + "; And assigned to groupId: " + ruleDTO.getGroupId();

        if (rule == null){
            description = Log.OBJECTNOTFOUND.format(id);
            logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
            return null;
        } 

        rule.setId(id);
        Rule updatedRule = ruleRepository.save(rule);
        eventPublisher.publishEvent(new RulesChangedEvent(this, updatedRule.getGroupId()));
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

        return updatedRule;
    }

    public Rule convertDTOToRule(RuleDTO ruleDTO) {
        String operation = "convertDTOToRule";
        String description = "Converted ruleDTO to Rule with groupId: " + ruleDTO.getGroupId();

        // check if group exists
        long groupId = ruleDTO.getGroupId();
        if (monitorGroupService.getGroupById(groupId) == null){
            description = Log.OBJECTNOTFOUND.format(groupId);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return null;
        }
        
        // check if template exists
        Template template = templateService.getTemplateById(ruleDTO.getTemplateId()).get();
        if (template == null){
            description = Log.OBJECTNOTFOUND.format(groupId);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
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
                description = "Something went wrong when adding chosenValues to Rule; exception: " + exception.getMessage();
                logsService.addLogEntry(Severity.ERROR, source, operation, description, logger);
                return null;
            }
        }
        
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        return rule;
    }

    public String ruleToHTML(Rule rule, float screenWidth, float screenHeight){

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("/static/widgets/");
        resolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        StringBuilder htmlBody = new StringBuilder();

        // sort the widgets by their zindex
        // so that the ones with lower zindex
        // get added first to the html
        List<TemplateWidget> sortedWidgets = rule.getTemplate().getWidgets();
        sortedWidgets.sort(new TemplateWidget.ZIndexComparator());

        for (TemplateWidget templateWidget : rule.getTemplate().getWidgets()){
            Widget widget = templateWidget.getWidget();

            Context context = new Context();
            context.setVariable("top", (templateWidget.getTop() * screenHeight) / 100);
            context.setVariable("left", (templateWidget.getLeft() * screenWidth) / 100);
            context.setVariable("width", (templateWidget.getWidth() * screenWidth) / 100);
            context.setVariable("height", (templateWidget.getHeight() * screenHeight) / 100);
            context.setVariable("widgetID", widget.hashCode());

            for (WidgetVariable variable : widget.getVariables()){
                Object chosenValue = rule.getChosenValue(templateWidget.getId(), variable.getName());
                Object defaultValue = templateWidget.getDefaultValue(variable.getName());
                if (chosenValue != null){
                    context.setVariable(variable.getName(), processVariable(variable, chosenValue));
                }
                else if (defaultValue != null){
                    context.setVariable(variable.getName(), processVariable(variable, defaultValue));
                }
            }

            htmlBody.append(templateEngine.process(widget.getName(), context));
        }

        Context baseContext = new Context();
        baseContext.setVariable("insertContent", htmlBody.toString());
        return templateEngine.process("base", baseContext);
    }

    private Object processVariable(WidgetVariable variable, Object variableValue){
        if(variable.getType() == WidgetVariableType.MEDIA){
            Integer fileId = (Integer) variableValue;
            CustomFile file = fileService.getFileById(Long.valueOf(fileId)).get();
            // If it is a directory return all children
            // separated by commas
            if ("directory".equals(file.getType())){
                String result = "";
                for(CustomFile child : file.getSubDirectories()){
                    result += child.getNameWithExtention() + ",";
                }
                result = result.substring(0, result.length() - 1);
                return result;
            }
            // If it is a file return it's name
            else{
                return file.getNameWithExtention();
            }
        }
        return variableValue;
    }

}
