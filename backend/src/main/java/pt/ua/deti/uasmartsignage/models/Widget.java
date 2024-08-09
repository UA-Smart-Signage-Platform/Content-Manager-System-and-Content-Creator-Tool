package pt.ua.deti.uasmartsignage.models;

import pt.ua.deti.uasmartsignage.enums.WidgetVariableType;
import pt.ua.deti.uasmartsignage.models.embedded.WidgetVariable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "widgets")
public class Widget {

    @Id
    private String id;

    private String name;

    private float minWidth;
    
    private float minHeight;

    @Builder.Default
    private Set<WidgetVariable> variables = new HashSet<>();

    public void addVariable(String name, WidgetVariableType type, List<String> optionsList){
        WidgetVariable variable = new WidgetVariable(name, type, optionsList);
        variables.add(variable);
    }

    public void addVariable(String name, WidgetVariableType type){
        WidgetVariable variable = new WidgetVariable(name, type, null);
        variables.add(variable);
    }

    public boolean hasVariable(String name){
        for (WidgetVariable variable : this.variables){
            if (variable.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}

