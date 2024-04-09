package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.CustomFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<CustomFile, Long> {
    CustomFile findByName(String fileName);
}
