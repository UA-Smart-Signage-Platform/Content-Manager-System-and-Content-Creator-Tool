package pt.ua.deti.uasmartsignage.services;

import pt.ua.deti.uasmartsignage.models.Rule;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.models.Widget;
import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;
import pt.ua.deti.uasmartsignage.models.embedded.WidgetVariable;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

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

    public List<Rule> getAllRulesForGroup(Long groupId) {
        List<Rule> rules = ruleRepository.findByGroupId(groupId);
        if (rules == null) return null;
        return rules;
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

    public String ruleToHTML(Rule rule, float screenWidth, float screenHeight){

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("/static/widgets/");
        resolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        StringBuilder htmlBody = new StringBuilder();

        // TODO: zindex
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

    // TODO: add logic for media variables
    private Object processVariable(WidgetVariable variable, Object variableValue){
        return variableValue;
    }

}
