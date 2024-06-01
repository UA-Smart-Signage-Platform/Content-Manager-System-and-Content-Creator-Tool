package deti.uas.uasmartsignage.Models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @JsonIgnoreProperties("templateGroups")
    private Template template;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    @JsonIgnoreProperties("templateGroups")
    private MonitorsGroup group;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TemplateGroup_Content", joinColumns = @JoinColumn(name = "template_group_id"))
    @MapKeyColumn(name = "content_key")
    @Column(name = "content_value")
    private Map<Integer, String> content;

    @ManyToOne
    @JoinColumn(name = "scheduleId", nullable = false)
    @JsonIgnoreProperties(value={"templateGroups"}, allowSetters = true)
    private Schedule schedule;
}
