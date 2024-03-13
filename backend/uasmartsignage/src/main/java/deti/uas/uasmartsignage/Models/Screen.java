package deti.uas.uasmartsignage.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "screen")
public class Screen {

    @Id
    private Long id;

    @Column(nullable = false)
    private String localização;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Long group_id;

}
