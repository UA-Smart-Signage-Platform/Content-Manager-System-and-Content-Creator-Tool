package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(nullable = false)
    private Long size;

    @Column
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("subDirectories")
    @JoinColumn(name = "parentId")
    private CustomFile parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("parent")
    private List<CustomFile> subDirectories;

    public CustomFile(String name, String type, Long size, CustomFile parent) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.parent = parent;
        this.subDirectories = null;
    }

    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", parent=" + (parent != null ? parent : "null") +
                ", subDirectories=" + subDirectories +
                '}';
    }
}
