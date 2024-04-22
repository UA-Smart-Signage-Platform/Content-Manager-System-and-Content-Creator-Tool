package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;
import deti.uas.uasmartsignage.Services.TemplateService;

@ExtendWith(MockitoExtension.class)

public class TemplateServiceTest {
    @Mock
    private TemplateRepository repository;

    @InjectMocks
    private TemplateService service;

    @Test
    void getTemplateByIdTestReturnsTemplate(){
        Template template = new Template();
        template.setName("template");
        when(repository.findById(1L)).thenReturn(Optional.of(template));

        Template template1 = service.getTemplateById(1L);

        assertThat(template1.getName()).isEqualTo("template");

    }

    @Test
    void whenServiceSaveThenRepositorySave(){
        Template template = new Template();
        template.setName("template");
        when(repository.save(template)).thenReturn(template);

        Template template1 = service.saveTemplate(template);

        assertThat(template1.getName()).isEqualTo("template");
    }

    @Test
    void whenFindAll_thenReturnAllTemplates(){
        Template template1 = new Template();
        template1.setName("template1");
        when(repository.findAll()).thenReturn(Arrays.asList(template1));

        Iterable<Template> templates = service.getAllTemplates();

        assertThat(templates).isNotEmpty();
        assertThat(templates).hasSize(1);
        assertThat(templates).contains(template1);
    }

    @Test
    void whenFindByName_thenReturnTemplate(){
        Template template = new Template();
        template.setName("template");
        when(repository.findByName(template.getName())).thenReturn(template);

        Template found = service.getTemplateByName(template.getName());

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(template);
    }


    @Test
    void whenDeleteTemplate_thenReturnEmpty() {
        Template template = new Template();
        template.setName("template");
        service.deleteTemplate(1L);
        verify(repository, times(1)).deleteById(1L);
        assertFalse(repository.existsById(1L));


    }

    @Test
    void whenUpdateTemplate_thenReturnUpdatedTemplate() {
        Template template = new Template();
        template.setName("template");
        when(repository.findById(1L)).thenReturn(Optional.of(template));
        Template template2 = new Template();
        template2.setName("template2");
        when(repository.save(template)).thenReturn(template2);
        Template updatedTemplate = service.updateTemplate(1L, template2);
        assertThat(updatedTemplate.getName()).isEqualTo("template2");
    }
}
