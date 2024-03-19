package deti.uas.uasmartsignage.Models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
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
    private List<Monitor> monitors;

    @OneToOne(mappedBy = "monitorsGroupForTemplate", cascade = CascadeType.ALL)
    private TemplateGroup templateGroup;

}
