package pt.ua.deti.uasmartsignage.repositories;

import pt.ua.deti.uasmartsignage.models.TemplateGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateGroupRepository extends JpaRepository<TemplateGroup, Long>{
    TemplateGroup findByGroupId(Long groupId);
}
