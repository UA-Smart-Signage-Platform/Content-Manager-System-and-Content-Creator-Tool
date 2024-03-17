package deti.uas.uasmartsignage.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.uas.uasmartsignage.Models.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{

    Group findByName(String name);

    Optional<Group> findById(Long id);


}
