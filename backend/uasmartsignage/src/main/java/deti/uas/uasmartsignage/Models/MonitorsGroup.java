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

    @Column
    private String description;

    @OneToMany(mappedBy = "group")
    @JsonIgnoreProperties("group")
    private List<Monitor> monitors;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private TemplateGroup templateGroup;

    @OneToMany(mappedBy = "monitorsGroupForSchedules", cascade = CascadeType.ALL)
   private List<Schedule> schedules;

    @Override
    public String toString() {
        return "MonitorsGroup [id=" + id + ", name=" + name
                + "]";
    }
}
