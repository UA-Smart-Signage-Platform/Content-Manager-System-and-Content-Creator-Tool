package pt.ua.deti.uasmartsignage.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import pt.ua.deti.uasmartsignage.models.Template;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String>{
    Template findByName(String name);
}
