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

    @Column
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private List<Monitor> monitors;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("group")
    private TemplateGroup templateGroup;

    @OneToMany(mappedBy = "monitorsgroupforshcedules", cascade = CascadeType.ALL)
    @JoinTable(
        name = "MonitorsGroup_Schedule",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
   private List<Schedule> schedules;

    @Override
    public String toString() {
        return "MonitorsGroup [id=" + id + ", name=" + name
                + "]";
    }
}
