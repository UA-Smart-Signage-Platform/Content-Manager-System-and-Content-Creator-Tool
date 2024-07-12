package pt.ua.deti.uasmartsignage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.deti.uasmartsignage.models.embedded.Schedule;

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

}

