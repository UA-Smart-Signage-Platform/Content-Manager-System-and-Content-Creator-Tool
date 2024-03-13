package deti.uas.uasmartsignage.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import deti.uas.uasmartsignage.Models.Content;

@Repository
public interface ContentRepository extends MongoRepository<Content, Long> {
    
}
