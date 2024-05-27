package deti.uas.uasmartsignage.repositoryTests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Repositories.TemplateWidgetRepository;
import deti.uas.uasmartsignage.Models.Widget;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TemplateWidgetRepositoryTest {
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
