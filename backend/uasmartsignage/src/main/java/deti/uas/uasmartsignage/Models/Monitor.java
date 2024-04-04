package deti.uas.uasmartsignage.Models;

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

    @Column(nullable = false, unique = true)
    private String location;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    @JsonIgnoreProperties("monitors")
    private MonitorsGroup monitorsGroupForScreens;

}
