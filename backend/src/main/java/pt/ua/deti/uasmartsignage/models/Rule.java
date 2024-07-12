package pt.ua.deti.uasmartsignage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.deti.uasmartsignage.models.embedded.Schedule;
import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;
import pt.ua.deti.uasmartsignage.models.embedded.WidgetVariable;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rules")
public class Rule {

    @Id
    private String id;
    private long groupId;
    private Template template;
    private Schedule schedule;

    // TODO - If the variable type is options, check if the options is in the optionsList
    public void setWidgetVariableValue(String widgetId, String name, String value) {
        if (this.template == null || this.template.getWidgets() == null) {
            throw new IllegalStateException("Template or Widgets List is null");
        }

        for (TemplateWidget templateWidget : this.template.getWidgets()) {

            Widget widget = templateWidget.getWidget();
            System.out.println(widget.getId() + " " + widgetId);
            if (widget != null && widget.getId().equals(widgetId)) {

                if (templateWidget.getWidget().getVariables() == null) {
                    throw new IllegalStateException("Widget contains no variables");
                }

                for (WidgetVariable variable : widget.getVariables()) {
                    System.out.println(variable.getName() + " " + name);
                    if (variable.getName().equals(name)) {
                        variable.setValue(value);
                        System.out.println(variable.getValue());
                    }
                }

            }
        }
    }

}
