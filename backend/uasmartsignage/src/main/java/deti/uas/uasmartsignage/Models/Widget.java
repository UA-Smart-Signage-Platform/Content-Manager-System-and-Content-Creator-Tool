package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "Widgets")
public class Widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    @OneToMany(mappedBy = "widget", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("widget")
    private List<Content> contents;
    
    @OneToMany(mappedBy = "widget", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value={"widget"},allowSetters = true)
    private List<TemplateWidget> templateWidgets;

}
