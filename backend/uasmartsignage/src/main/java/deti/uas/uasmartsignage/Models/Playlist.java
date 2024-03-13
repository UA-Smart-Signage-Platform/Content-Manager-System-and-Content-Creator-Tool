package deti.uas.uasmartsignage.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    private Long id;


    @ManyToMany
    @JoinTable(name="playlist_content",
            joinColumns = @JoinColumn(name="playlist_id"),
            inverseJoinColumns = @JoinColumn(name="content_id")
    )
    private List<Content> content;

    private Timestamp start_date;

    private Timestamp end_date;



}
