package deti.uas.uasmartsignage.Models;

import lombok.Data;
import java.sql.Timestamp;
import java.util.HashMap;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Playlists")
@Data
public class Playlist {

    @Id
    private String id;

    private String name;

    private String description;

    private HashMap<Content,Timestamp> Schedule;
}
