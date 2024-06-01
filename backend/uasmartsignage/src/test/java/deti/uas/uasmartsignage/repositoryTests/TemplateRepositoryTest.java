package deti.uas.uasmartsignage.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TemplateRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemplateRepository repository;

    @Test
    void whenFindAll_thenReturnAllTemplates(){
        Template template1 = new Template(null, "template1", List.of(), List.of());
        entityManager.persistAndFlush(template1);

        Template template2 = new Template();
        template2.setName("template2");
        entityManager.persistAndFlush(template2);

        List<Template> found = repository.findAll();

        assertThat(found).isNotEmpty().hasSize(2).contains(template1, template2);
    }

    @Test
    void whenFindByName_thenReturnTemplate(){
        Template template = new Template();
        template.setName("template");
        entityManager.persistAndFlush(template);

        Template found = repository.findByName(template.getName());

        assertThat(found).isNotNull().isEqualTo(template);
    }

    @Test
    void whenFindById_thenReturnTemplate(){
        Template template = new Template();
        template.setName("template");
        entityManager.persistAndFlush(template);

        Template found = repository.findById(template.getId()).orElse(null);

        assertThat(found).isNotNull().isEqualTo(template);
    }


}
