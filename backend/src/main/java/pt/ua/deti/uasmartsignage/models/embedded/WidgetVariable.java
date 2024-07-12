package pt.ua.deti.uasmartsignage.models.embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WidgetVariable {

    private String name;
    private String value;
    private String defaultValue;
    private String type;
    private List<String> optionsList;

    public WidgetVariable(String name, String type, String defaultValue){
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public WidgetVariable(String name, String type, String defaultValue, List<String> optionsList){
        this.name = name;
        this.type = type;
        this.optionsList = optionsList;
        this.defaultValue = defaultValue;
    }
    
}
