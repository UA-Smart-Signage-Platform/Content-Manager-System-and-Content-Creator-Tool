package pt.ua.deti.uasmartsignage.repositoryTests;

import pt.ua.deti.uasmartsignage.Models.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import pt.ua.deti.uasmartsignage.Models.Widget;
import pt.ua.deti.uasmartsignage.Repositories.WidgetRepository;
import pt.ua.deti.uasmartsignage.Models.Content;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WidgetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WidgetRepository repository;

    @Test
    void whenFindAll_thenReturnAllWidgets(){
        Content content = new Content();
        content.setName("content");
        content.setType("type");
        entityManager.persistAndFlush(content);

        Template template = new Template();
        template.setName("template");
        entityManager.persistAndFlush(template);

        Widget widget1 = new Widget();
        widget1.setName("widget1");
        widget1.setPath("path");
        widget1.setContents(List.of(content));
        entityManager.persistAndFlush(widget1);

        Widget widget2 = new Widget(null, "widget2", "path", List.of(content), List.of());
        entityManager.persistAndFlush(widget2);

        List<Widget> found = repository.findAll();

        assertThat(found).hasSize(2).extracting(Widget::getName).contains("widget1", "widget2");
    }

    @Test
    void whenFindById_thenReturnWidget() {
        Content content = new Content();
        content.setName("content");
        content.setType("type");
        entityManager.persistAndFlush(content);

        Template template = new Template();
        template.setName("template");
        entityManager.persistAndFlush(template);

        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        widget.setContents(List.of(content));
        entityManager.persistAndFlush(widget);

        Widget found = repository.findById(widget.getId()).get();

        assertThat(found).isEqualTo(widget);
    }

    @Test
    void whenDeleteWidget_thenReturnEmpty() {
        Content content = new Content();
        content.setName("content");
        content.setType("type");
        entityManager.persistAndFlush(content);

        Template template = new Template();
        template.setName("template");
        entityManager.persistAndFlush(template);

        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        widget.setContents(List.of(content));
        entityManager.persistAndFlush(widget);

        repository.delete(widget);
        Widget found = repository.findById(widget.getId()).orElse(null);

        assertThat(found).isNull();
    }
}
