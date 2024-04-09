package deti.uas.uasmartsignage.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.uas.uasmartsignage.Models.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>{
    
}
