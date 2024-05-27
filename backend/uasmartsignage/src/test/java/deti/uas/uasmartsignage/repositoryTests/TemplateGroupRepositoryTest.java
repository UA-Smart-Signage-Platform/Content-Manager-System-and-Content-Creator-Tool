package deti.uas.uasmartsignage.repositoryTests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import deti.uas.uasmartsignage.Models.Schedule;
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

import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Repositories.TemplateGroupRepository;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.MonitorsGroup;

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
        Schedule schedule = new Schedule();
        schedule.setPriority(1);
        entityManager.persistAndFlush(schedule);

        Template template1 = new Template();
        template1.setName("template1");
        entityManager.persistAndFlush(template1);

        Template template2 = new Template();
        template2.setName("template2");
        entityManager.persistAndFlush(template2);

        MonitorsGroup group1 = new MonitorsGroup();
        group1.setName("group1");
        entityManager.persistAndFlush(group1);

        MonitorsGroup group2 = new MonitorsGroup();
        group2.setName("group2");
        entityManager.persistAndFlush(group2);

        TemplateGroup templateGroup1 = new TemplateGroup();
        templateGroup1.setTemplate(template1);
        templateGroup1.setGroup(group1);
        templateGroup1.setContent(Map.of(1, "content1"));
        templateGroup1.setSchedule(schedule);
        entityManager.persistAndFlush(templateGroup1);

        TemplateGroup templateGroup2 = new TemplateGroup();
        templateGroup2.setTemplate(template1);
        templateGroup2.setGroup(group2);
        templateGroup2.setContent(Map.of(2, "content2"));
        templateGroup2.setSchedule(schedule);
        entityManager.persistAndFlush(templateGroup2);

        List<TemplateGroup> found = repository.findAll();

        assertThat(found).isNotEmpty();
        assertThat(found).hasSize(2);
        assertThat(found).contains(templateGroup1, templateGroup2);
    }

    @Test
    void testFindByGroupId(){
        Schedule schedule = new Schedule();
        schedule.setPriority(1);
        entityManager.persistAndFlush(schedule);

        Template template1 = new Template();
        template1.setName("template1");
        entityManager.persistAndFlush(template1);

        MonitorsGroup group1 = new MonitorsGroup();
        group1.setName("group1");
        entityManager.persistAndFlush(group1);

        MonitorsGroup group2 = new MonitorsGroup();
        group2.setName("group2");
        entityManager.persistAndFlush(group2);

        TemplateGroup templateGroup1 = new TemplateGroup();
        templateGroup1.setTemplate(template1);
        templateGroup1.setGroup(group1);
        templateGroup1.setContent(Map.of(1, "content1"));
        templateGroup1.setSchedule(schedule);
        entityManager.persistAndFlush(templateGroup1);

        TemplateGroup found = repository.findByGroupId(group1.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(templateGroup1);
    }

    @Test
    void testSaveTemplateGroup(){
        Schedule schedule = new Schedule();
        schedule.setPriority(1);
        entityManager.persistAndFlush(schedule);

        Template template1 = new Template();
        template1.setName("template1");
        entityManager.persistAndFlush(template1);

        MonitorsGroup group1 = new MonitorsGroup();
        group1.setName("group1");
        entityManager.persistAndFlush(group1);

        TemplateGroup templateGroup1 = new TemplateGroup();
        templateGroup1.setTemplate(template1);
        templateGroup1.setGroup(group1);
        templateGroup1.setContent(Map.of(1, "content1"));
        templateGroup1.setSchedule(schedule);

        TemplateGroup saved = repository.save(templateGroup1);

        assertThat(saved).isNotNull();
        assertThat(saved).isEqualTo(templateGroup1);
    }

}