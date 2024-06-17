package pt.ua.deti.uasmartsignage.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Monitors")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private int width;

    @Column(nullable = true)
    private int height;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private boolean pending;

    @Column
    private boolean online;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    @JsonIgnoreProperties({"monitors", "templateGroups"})
    private MonitorsGroup group;


    @Override
    public String toString() {
        return "Monitor [id=" + id + ", name=" + name + ", width=" + width + ", height=" + height + ", uuid=" + uuid + ", pending=" + pending + ", online=" + online + ", group=" + group + "]";
    }
}
