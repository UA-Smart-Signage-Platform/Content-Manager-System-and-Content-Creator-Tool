package deti.uas.uasmartsignage.Models;

;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Contents")
@Data
public class Content {

    @Id
    private Long id;

    @Indexed(unique = true)
    private String name;

    private String path;

    private String type;

    private String description;

    private String duration;

    private String size;
}
