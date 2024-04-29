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

    @OneToMany(mappedBy = "group")
    @JsonIgnoreProperties(value = {"group"},allowSetters = true)
    private List<Monitor> monitors;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private TemplateGroup templateGroup;

    @OneToMany(mappedBy = "monitorsGroupForSchedules", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("monitorsGroupForSchedules")
    private List<Schedule> schedules;

    @Override
    public String toString() {
        return "MonitorsGroup [id=" + id + ", name=" + name
                + "]";
    }
}
