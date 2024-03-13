package deti.uas.uasmartsignage.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "Widgets")
@Data
public class Widget {

    @Id
    private Long id;

    private String name;

    private Long top;

    private Long left;

    private Long width;

    private Long height;

    private List<Playlist> playlists;
}
