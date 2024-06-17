package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.TemplateGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateGroupRepository extends JpaRepository<TemplateGroup, Long>{
    TemplateGroup findByGroupId(Long groupId);
}
