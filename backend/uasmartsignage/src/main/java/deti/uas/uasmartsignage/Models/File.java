package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "File")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "parentId")
    private File parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<File> subDirectories;

    public File(String name, String type, File parent, List<File> subDirectories) {
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.subDirectories = subDirectories;
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
