package deti.uas.uasmartsignage.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.aggregation.DateOperators;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content {

    @Id
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToMany
    @JoinTable(name="content_playlist",
            joinColumns = @JoinColumn(name="content_id"),
            inverseJoinColumns = @JoinColumn(name="playlist_id")
    )
    private List<Playlist> playlist;

    private Timestamp start_date;

    private Timestamp end_date;


}
