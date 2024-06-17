package pt.ua.deti.uasmartsignage.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.deti.uasmartsignage.models.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>{
    
}
