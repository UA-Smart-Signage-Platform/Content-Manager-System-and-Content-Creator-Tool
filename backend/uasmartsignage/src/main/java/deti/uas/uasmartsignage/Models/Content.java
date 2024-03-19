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
@Table(name = "Content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String type;

    private String description;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Widget> widgets;

    
    @ElementCollection
    @CollectionTable(name = "Content_Options", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "option")
    private List<String> options;

}
