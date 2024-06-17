package pt.ua.deti.uasmartsignage.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value={"template","widgets"},allowSetters = true) //widegts
    private List<TemplateWidget> templateWidgets;

    @OneToMany(mappedBy = "template")
    @JsonIgnoreProperties(value={"template"},allowSetters = true)
    private List<TemplateGroup> templateGroups;

}


