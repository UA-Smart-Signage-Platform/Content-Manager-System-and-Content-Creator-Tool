package pt.ua.deti.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.repositories.FileRepository;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    FileRepository repository;

    CustomFile customFile;
    CustomFile customFile2;

    @BeforeEach
    void setUp(){
        customFile = new CustomFile("New directory", UUID.randomUUID().toString(), "directory", "", 0L, null);
        customFile2 = new CustomFile("Old directory", UUID.randomUUID().toString(), "directory", "", 0L, customFile);

        entityManager.persist(customFile);
        entityManager.persistAndFlush(customFile2);
    }

    @Test
    void givenValidId_whenFindFileById_thenReturnFile() {
        Optional<CustomFile> found = repository.findById(customFile.getId());
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(customFile);
    }

    @Test
    void givenValidName_whenFindByName_thenReturnFile() {
        Optional<CustomFile> found = repository.findByName("New directory");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("New directory");
    }

    @Test
    void givenMultipleFiles_whenFindAll_thenReturnAllFiles() {
        List<CustomFile> customFiles = repository.findAll();
        assertThat(customFiles).hasSize(2)
                                .extracting(CustomFile::getName)
                                .containsOnly(customFile.getName(), customFile2.getName());
    }

    @Test
    void givenFilesWithAndWithoutParent_whenFindAllByParentIsNull_thenReturnFilesWithoutParents() {
        List<CustomFile> customFiles = repository.findAllByParentIsNull();
        assertThat(customFiles).hasSize(1)
                                .extracting(CustomFile::getName)
                                .containsOnly(customFile.getName());
    }

    @Test
    void givenValidFile_whenDelete_thenFileIsDeleted() {
        repository.delete(customFile);

        Optional<CustomFile> found = repository.findById(customFile.getId());
        assertThat(found).isEmpty();
    }
}