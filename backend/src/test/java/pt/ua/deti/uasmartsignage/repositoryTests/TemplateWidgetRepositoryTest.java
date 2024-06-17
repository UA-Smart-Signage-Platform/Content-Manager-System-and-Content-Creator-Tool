package pt.ua.deti.uasmartsignage.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import pt.ua.deti.uasmartsignage.models.TemplateWidget;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.Repositories.TemplateWidgetRepository;
import pt.ua.deti.uasmartsignage.models.Widget;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TemplateWidgetRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemplateWidgetRepository repository;

    @Test
    void testFindAll(){
        Template template1 = new Template();
        template1.setName("template1");
        entityManager.persistAndFlush(template1);

        Widget widget1 = new Widget();
        widget1.setName("widget1");
        entityManager.persistAndFlush(widget1);

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);
        entityManager.persistAndFlush(templateWidget1);

        Template template2 = new Template();
        template2.setName("template2");
        entityManager.persistAndFlush(template2);

        Widget widget2 = new Widget();
        widget2.setName("widget2");
        entityManager.persistAndFlush(widget2);

        TemplateWidget templateWidget2 = new TemplateWidget(null, 2,2,2,2,1,template2,widget2);
        entityManager.persistAndFlush(templateWidget2);

        List<TemplateWidget> templateWidgets = repository.findAll();

        assertThat(templateWidgets).hasSize(2).contains(templateWidget1, templateWidget2);
    }
}
