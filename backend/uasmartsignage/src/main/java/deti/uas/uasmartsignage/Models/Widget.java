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
@Table(name = "widget")
public class Widget {

    @Id
    private Long id;

    @Column(nullable = false)
    private String path;


}
