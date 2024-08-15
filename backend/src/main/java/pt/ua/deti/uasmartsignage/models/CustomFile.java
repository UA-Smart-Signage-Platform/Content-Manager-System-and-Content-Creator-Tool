package pt.ua.deti.uasmartsignage.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "File")
@JsonIgnoreProperties({"parent", "subDirectories"})
public class CustomFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private CustomFile parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomFile> subDirectories;

    public CustomFile(String name, String uuid, String type, String extension, Long size, CustomFile parent) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
        this.extension = extension;
        this.size = size;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +     
                ", type='" + type + '\'' +
                ", extension='" + extension + '\'' +
                ", parent=" + (parent != null ? parent : "null") +
                ", subDirectories=" + (subDirectories == null ? "null" : Arrays.toString(subDirectories.toArray())) +
                '}';
    }
}
