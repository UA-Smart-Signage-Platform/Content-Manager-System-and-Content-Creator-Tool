package deti.uas.uasmartsignage.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Screens")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ip;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private boolean pending;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    private MonitorsGroup group;

    public Monitor(String ip, String name, int size){
        this.ip = ip;
        this.name = name;
        this.size = size;
        this.pending = true;
    }

}
