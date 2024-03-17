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
@Table(name = "Widgets")
public class Widget {

    @Id
    private Long id;

    private String name;

    private String path;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    @JsonIgnore
    private Content content;

    @ManyToMany(mappedBy = "widgets")
    @JsonIgnore
    private List<Template_Widget> template_widgets;

}
