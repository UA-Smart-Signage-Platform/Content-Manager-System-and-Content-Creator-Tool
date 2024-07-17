package pt.ua.deti.uasmartsignage.repositories;

import pt.ua.deti.uasmartsignage.models.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {
}
