package deti.uas.uasmartsignage.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Repositories.ContentRepository;
import deti.uas.uasmartsignage.Models.Widget;

@DataJpaTest
public class ContentRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ContentRepository repository;

    @Test
    void whenFindById_thenReturnContent() {
        Widget widget = new Widget();
        widget.setName("New Widget");

        entityManager.persistAndFlush(widget);

        Content content = new Content();
        content.setName("New Content");
        content.setType("type");
        content.setWidget(widget);
        content.setOptions(new ArrayList<>());
        entityManager.persistAndFlush(content);

        Content found = repository.findById(content.getId()).get();

        assertThat(found).isEqualTo(content);
    }

    @Test
    void whenFindByInvalidId_thenReturnEmpty() {
        Optional<Content> found = repository.findById(10L);
        assertThat(found).isEmpty();
    }

    @Test
    void whenFindAll_thenReturnAllContent() {
        Widget widget = new Widget();
        widget.setName("New Widget");
        entityManager.persistAndFlush(widget);

        Content content1 = new Content();
        content1.setName("New Content 1");
        content1.setType("type");
        content1.setWidget(widget);
        content1.setOptions(new ArrayList<>());
        entityManager.persistAndFlush(content1);

        Content content2 = new Content();
        content2.setName("New Content 2");
        content2.setType("type");
        content2.setWidget(widget);
        content2.setOptions(new ArrayList<>());
        entityManager.persistAndFlush(content2);

        List<Content> found = repository.findAll();

        assertThat(found).contains(content1, content2);
    }

    @Test
    void whenDeleteContent_thenReturnAllContent() {
        Content content1 = new Content();
        content1.setName("New Content 1");
        content1.setType("type");
        content1.setWidget(null);
        content1.setOptions(new ArrayList<>());
        entityManager.persistAndFlush(content1);

        List<Content> list_content = repository.findAll();

        assertThat(list_content).hasSize(1);

        entityManager.remove(content1);

        list_content = repository.findAll();

        assertThat(list_content).hasSize(0);
    }


}
