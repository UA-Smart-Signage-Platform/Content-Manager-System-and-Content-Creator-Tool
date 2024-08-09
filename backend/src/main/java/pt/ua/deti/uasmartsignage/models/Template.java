package pt.ua.deti.uasmartsignage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "templates")
public class Template {

    @Id
    private String id;

    private String name;

    @Builder.Default
    private List<TemplateWidget> widgets = new ArrayList<>();

    public TemplateWidget addWidget(Widget widget, float top, float left, float width, float height, int zindex, Map<String, Object> defaultValues){

        TemplateWidget templateWidget = new TemplateWidget(widget, top, left, width, height, zindex);
        if (defaultValues != null){
            for(Map.Entry<String, Object> entry : defaultValues.entrySet()){
                if (widget.hasVariable(entry.getKey())){
                    templateWidget.setDefaultValue(entry.getKey(), entry.getValue());
                }
                else{
                    throw new IllegalArgumentException("Invalid variable");
                }
            }
        }
        
        widgets.add(templateWidget);
        return templateWidget;
    }

    public TemplateWidget addWidget(Widget widget, float top, float left, float width, float height, int zindex){
        TemplateWidget templateWidget = new TemplateWidget(widget, top, left, width, height, zindex);        
        widgets.add(templateWidget);
        return templateWidget;
    }
}
