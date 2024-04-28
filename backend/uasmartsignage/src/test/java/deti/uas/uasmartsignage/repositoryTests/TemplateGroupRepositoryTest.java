package deti.uas.uasmartsignage.repositoryTests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TemplateGroupRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemplateGroupRepository repository;

    @Test
    void whenFindAll_thenReturnAllTemplateGroups(){
        TemplateGroup templateGroup1 = new TemplateGroup();
        //templateGroup1.setName("templateGroup1");
        entityManager.persistAndFlush(templateGroup1);

        TemplateGroup templateGroup2 = new TemplateGroup();
        //templateGroup2.setName("templateGroup2");
        entityManager.persistAndFlush(templateGroup2);

        List<TemplateGroup> found = repository.findAll();

        assertThat(found).isNotEmpty();
        assertThat(found).hasSize(2);
        assertThat(found).contains(templateGroup1, templateGroup2);
    }
}
