package pt.ua.deti.uasmartsignage.models.embedded;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Variable {

    private String name;
    private String value;
    private String type;
    private List<String> optionsList;

    public Variable(String name, String type){
        this.name = name;
        this.type = type;
    }

    public Variable(String name, String type, List<String> optionsList){
        this.name = name;
        this.type = type;
        this.optionsList = optionsList;
    }
    
}
