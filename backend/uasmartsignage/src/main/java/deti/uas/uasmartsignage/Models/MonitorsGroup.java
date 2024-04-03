package deti.uas.uasmartsignage.Models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MonitorsGroup")
public class MonitorsGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private List<Monitor> monitors;

    @OneToOne(mappedBy = "monitorsGroupForTemplate", cascade = CascadeType.ALL)
    private TemplateGroup templateGroup;

    public MonitorsGroup(String name, String description){
        this.name = name;
        this.description = description;
        this.monitors = new ArrayList<>();
    }
}
