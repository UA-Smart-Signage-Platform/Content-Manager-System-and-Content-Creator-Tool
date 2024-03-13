package deti.uas.uasmartsignage.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Playlist")
@Data
public class Playlist implements Serializable{

    
    private Long id;


    private Timestamp start_date;

    private Timestamp end_date;

    



}
