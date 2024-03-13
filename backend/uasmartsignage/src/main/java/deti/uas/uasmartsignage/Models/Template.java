package deti.uas.uasmartsignage.Models;


import jakarta.persistence.*;
import lombok.*;
import deti.uas.uasmartsignage.Models.Widget;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "template")
@Data
public class Template {

    @Id
    private Long id;

    private String path;

    
    private List<Widget> widgets;

    

}
