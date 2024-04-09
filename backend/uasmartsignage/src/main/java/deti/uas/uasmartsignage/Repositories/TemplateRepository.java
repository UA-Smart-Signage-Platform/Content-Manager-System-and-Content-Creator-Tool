package deti.uas.uasmartsignage.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import deti.uas.uasmartsignage.Models.Template;

public interface TemplateRepository extends JpaRepository<Template, Long>{

    
    Template findByName(String name);
}
