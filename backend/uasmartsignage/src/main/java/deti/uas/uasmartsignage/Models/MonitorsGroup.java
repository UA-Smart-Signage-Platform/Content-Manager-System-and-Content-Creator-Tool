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

    @OneToMany(mappedBy = "monitorsGroupForScreens", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("monitorsGroupForScreens")
    private List<Monitor> monitors;

    @OneToOne(mappedBy = "monitorsGroupForTemplate", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("monitorsGroupForTemplate")
    private TemplateGroup templateGroup;

    @Override
    public String toString() {
        return "MonitorsGroup [id=" + id + ", name=" + name
                + "]";
    }
}
