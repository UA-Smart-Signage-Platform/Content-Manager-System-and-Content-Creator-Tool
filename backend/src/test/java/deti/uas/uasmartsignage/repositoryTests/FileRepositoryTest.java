package pt.ua.deti.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import pt.ua.deti.uasmartsignage.Models.CustomFile;
import pt.ua.deti.uasmartsignage.Repositories.FileRepository;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    FileRepository repository;


    @Test
    void whenFindById_thenReturnFile() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);
        CustomFile saved = new CustomFile();

        entityManager.persistAndFlush(customFile);

        Optional<CustomFile> found = repository.findById(customFile.getId());
        if (found.isPresent()) {
            saved = found.get();
        }

        assertThat(saved).isEqualTo(customFile);
    }

    @Test
    void whenFindByInvalidId_thenReturnEmpty() {
        Optional<CustomFile> found = repository.findById(10L);
        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByName_thenReturnFile() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);
        CustomFile saved = new CustomFile();

        entityManager.persistAndFlush(customFile);

        Optional<CustomFile> found = repository.findByName(customFile.getName());
        if (found.isPresent()) {
            saved = found.get();
        }
        assertThat(saved).isEqualTo(customFile);
    }

    @Test
    void whenFindByInvalidName_thenReturnNull() {
        Optional<CustomFile> found = repository.findByName("New directory");
        assertThat(found).isEmpty();
    }

    @Test
    void whenFindAllFiles_thenReturnAllFiles() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);
        CustomFile customFile2 = new CustomFile("A normal file", "image", 200L, customFile);

        entityManager.persist(customFile);
        entityManager.persistAndFlush(customFile2);

        List<CustomFile> customFiles = repository.findAll();

        assertThat(customFiles).hasSize(2)
                                .extracting(CustomFile::getName)
                                .containsOnly(customFile.getName(), customFile2.getName());
    }

    @Test
    void whenDeleteFile_thenReturnEmpty() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);

        entityManager.persistAndFlush(customFile);

        repository.delete(customFile);

        Optional<CustomFile> found = repository.findById(customFile.getId());
        assertThat(found).isEmpty();
    }

}