package deti.uas.uasmartsignage.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MonitorsGroup")
public class MonitorsGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean madeForMonitor;

    private String description;

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"group"},allowSetters = true)
    private List<Monitor> monitors;

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"group"})
    private List<TemplateGroup> templateGroups;

    @Override
    public String toString() {
        return "MonitorsGroup [id=" + id + ", name=" + name + "]";
    }
}
