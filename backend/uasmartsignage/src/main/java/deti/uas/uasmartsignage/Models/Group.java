package deti.uas.uasmartsignage.Models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Screen> screens;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    private Template_Group template_group;

    
    
}
