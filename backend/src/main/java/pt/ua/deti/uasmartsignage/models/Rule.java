package pt.ua.deti.uasmartsignage.models;

import pt.ua.deti.uasmartsignage.models.embedded.Schedule;
import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rules")
public class Rule {

    @Id
    private String id;

    private long groupId;

    private Template template;

    private Schedule schedule;

    @Builder.Default
    private Map<Long, Map<String, Object>> chosenValues = new HashMap<>(); 

    public void setChosenValue(Long templateWidgetId, String name, Object value) {

        if (!isValidWidgetVariable(templateWidgetId, name)){
            throw new IllegalArgumentException("Invalid widgetId or invalid variable name provided");
        }

        if (chosenValues.containsKey(templateWidgetId)){
            chosenValues.get(templateWidgetId).put(name, value);
        }
        else{
            Map<String, Object> variables = new HashMap<>();
            variables.put(name, value);
            chosenValues.put(templateWidgetId, variables);
        }
    }

    // TODO if the variable type is "options", check if the value is an option
    public boolean isValidWidgetVariable(Long templateWidgetId, String name) {
        if (template == null || template.getWidgets() == null) {
            return false;
        }
        
        // Check if there is a template widget with the given id
        // and if it contains a variable with the given name
        return template.getWidgets().stream()
                .filter(widget -> widget != null && widget.getId() == templateWidgetId)
                .map(TemplateWidget::getWidget)
                .anyMatch(widget -> widget.getVariables() != null &&
                                    widget.getVariables().stream()
                                            .anyMatch(variable -> variable.getName().equals(name)));
    }
}
