package pt.ua.deti.uasmartsignage.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MonitorsGroup")
public class MonitorGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean isDefaultGroup;

    private String description;

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"group"},allowSetters = true)
    private List<Monitor> monitors;

    @Override
    public String toString() {
        return "MonitorGroup [id=" + id + ", name=" + name + "]";
    }
}