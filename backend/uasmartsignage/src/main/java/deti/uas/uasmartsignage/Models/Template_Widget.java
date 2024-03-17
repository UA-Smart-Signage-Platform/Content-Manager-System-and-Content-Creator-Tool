package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.ManyToMany;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Template_Widgets")
public class Template_Widget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long top;

    @Column(nullable = false)
    private Long left;

    @Column(nullable = false)
    private Long width;

    @Column(nullable = false)
    private Long height;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    @JsonIgnore
    private Template template;

    @ManyToMany
    @JoinTable(
            name = "Template_Widget_Widgets",
            joinColumns = @JoinColumn(name = "template_widget_id"),
            inverseJoinColumns = @JoinColumn(name = "widget_id")
    )
    private List<Widget> widgets;
}
