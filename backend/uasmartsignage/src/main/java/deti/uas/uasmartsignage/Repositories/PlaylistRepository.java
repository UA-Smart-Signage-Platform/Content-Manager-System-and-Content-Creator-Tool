package deti.uas.uasmartsignage.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import deti.uas.uasmartsignage.Models.Playlist;

public interface PlaylistRepository extends MongoRepository<Playlist, Long>{

    
}
