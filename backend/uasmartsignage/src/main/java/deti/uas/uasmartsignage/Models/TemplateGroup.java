package deti.uas.uasmartsignage.Models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TemplateGroups")
public class TemplateGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "templateId", nullable = false)
    @JsonIgnore
    private Template template;

    @OneToOne
    @JoinColumn(name = "groupId", nullable = false)
    @JsonIgnore
    private MonitorsGroup monitorsGroupForTemplate;

    @ElementCollection
    @CollectionTable(name = "TemplateGroup_Content", joinColumns = @JoinColumn(name = "template_group_id"))
    @MapKeyColumn(name = "content_key")
    @Column(name = "content_value")
    Map<Integer, String> content;


}
