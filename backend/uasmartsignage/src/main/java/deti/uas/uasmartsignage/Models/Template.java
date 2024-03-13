package deti.uas.uasmartsignage.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Templates")
@Data
public class Template {

    @Id
    private String id;

    private String path;

    @Indexed(unique = true)
    private String name;

    private List<Widget> widgets;

}


