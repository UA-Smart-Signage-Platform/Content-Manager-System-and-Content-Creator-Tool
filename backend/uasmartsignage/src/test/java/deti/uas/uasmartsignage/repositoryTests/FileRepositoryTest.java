package deti.uas.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Repositories.FileRepository;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    FileRepository repository;

  // TODO - create and revise tests

    @Test
    void whenFindById_thenReturnFile() {
        CustomFile customFile = new CustomFile();
        customFile.setName("New Directory");
        customFile.setType("directory");
        customFile.setSize(0L);
        customFile.setParent(null);
        customFile.setSubDirectories(new ArrayList<>());

        entityManager.persistAndFlush(customFile);

        CustomFile found = repository.findById(customFile.getId()).get();

        assertThat(found).isEqualTo(customFile);
    }

    @Test
    void whenFindByInvalidId_thenReturnEmpty() {
        Optional<CustomFile> found = repository.findById(10L);
        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByName_thenReturnFile() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);

        entityManager.persistAndFlush(customFile);

        CustomFile found = repository.findByName(customFile.getName());

        assertThat(found).isEqualTo(customFile);
    }

    @Test
    void whenFindByInvalidName_thenReturnNull() {
        CustomFile found = repository.findByName("New directory");
        assertThat(found).isNull();
    }

    @Test
    void whenFindAllFiles_thenReturnAllMonitors() {
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

        List<CustomFile> customFiles = repository.findAll();

        assertThat(customFiles).hasSize(1);

        entityManager.remove(customFile);

        customFiles = repository.findAll();

        assertThat(customFiles).hasSize(0);
    }

}