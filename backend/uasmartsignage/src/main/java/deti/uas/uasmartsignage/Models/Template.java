package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String id;

    private String path;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TemplateWidget> templateWidgets;

}


