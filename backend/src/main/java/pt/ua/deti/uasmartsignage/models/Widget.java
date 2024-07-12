package pt.ua.deti.uasmartsignage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.deti.uasmartsignage.models.embedded.WidgetVariable;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "widgets")
public class Widget {

    @Id
    private String id;
    private String name;
    private String path;
    private List<WidgetVariable> variables;

    public Widget(String name, String path){
        this.name = name;
        this.path = path;
    }
}

