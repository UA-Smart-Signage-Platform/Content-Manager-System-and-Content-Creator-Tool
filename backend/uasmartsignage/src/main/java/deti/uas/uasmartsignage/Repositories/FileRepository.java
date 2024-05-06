package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.CustomFile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<CustomFile, Long> {
    Optional<CustomFile> findByName(String fileName);

    @Query("SELECT file FROM CustomFile file WHERE file.parent IS NULL")
    List<CustomFile> findAllByParentIsNull();
}
