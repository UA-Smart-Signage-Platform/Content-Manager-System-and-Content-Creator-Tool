package deti.uas.uasmartsignage.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import deti.uas.uasmartsignage.Models.Template;

public interface TemplateRepository extends MongoRepository<Template, Long>{

    
}
