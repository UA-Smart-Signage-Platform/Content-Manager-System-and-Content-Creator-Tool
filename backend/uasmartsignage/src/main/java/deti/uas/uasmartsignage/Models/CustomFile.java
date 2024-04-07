package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "File")
public class CustomFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "parentId")
    private CustomFile parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<CustomFile> subDirectories;

    public CustomFile(String name, String type, CustomFile parent, List<CustomFile> subDirectories) {
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.subDirectories = List.of();
    }

    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", parent=" + parent +
                ", subDirectories=" + subDirectories +
                '}';
    }
}
