package pt.ua.deti.uasmartsignage.models;

import pt.ua.deti.uasmartsignage.enums.WidgetVariableType;
import pt.ua.deti.uasmartsignage.models.embedded.Schedule;
import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    public Object getChosenValue(Long templateWidgetId, String name) {

        if (!isValidWidgetVariable(templateWidgetId, name)){
            throw new IllegalArgumentException("Invalid widgetId or invalid variable name provided");
        }

        if (chosenValues.containsKey(templateWidgetId)){
            if (chosenValues.get(templateWidgetId).containsKey(name)){
                return chosenValues.get(templateWidgetId).get(name);
            }
        }
        
        return null;
    }

    public List<Long> getMediaWidgetValues() {
        if (template == null || template.getWidgets() == null) {
            return Collections.emptyList(); // Return an empty list if no template or widgets are found
        }
    
        List<Long> mediaValues = new ArrayList<>();
    
        // Loop through the chosenValues map, checking if the variable name has type 'media'
        for (Map.Entry<Long, Map<String, Object>> entry : chosenValues.entrySet()) {
            Long templateWidgetId = entry.getKey();
            Map<String, Object> variablesMap = entry.getValue();
    
            // Get the template widget that matches the given templateWidgetId
            template.getWidgets().stream()
                .filter(widget -> widget != null && widget.getId() == templateWidgetId)
                .findFirst() // Ensure we get only the first match
                .ifPresent(widget -> {
                    // Loop through each variable in the variables map
                    variablesMap.forEach((variableName, value) -> {
                        // Check if the widget contains a variable with the given name and type 'media'
                        widget.getWidget().getVariables().stream()
                            .filter(variable -> variable.getName().equals(variableName) && WidgetVariableType.MEDIA.equals(variable.getType()))
                            .findFirst() // Ensure we get the correct variable
                            .ifPresent(variable -> {
                                // Add the value to the mediaValues list
                                Integer mediaValue = (Integer) value;
                                mediaValues.add(Long.valueOf(mediaValue));
                            });
                    });
                });
        }
    
        return mediaValues;
    }
    

    // TODO if the variable type is "options", check if the value is an option
    // TODO if the variable type is "media", check if the file exists
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
