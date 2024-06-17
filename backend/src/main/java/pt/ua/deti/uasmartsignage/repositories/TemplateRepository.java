package pt.ua.deti.uasmartsignage.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import pt.ua.deti.uasmartsignage.models.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long>{
    Template findByName(String name);
}
