package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Repositories.TemplateWidgetRepository;
import deti.uas.uasmartsignage.Services.TemplateWidgetService;
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
class TemplateWidgetServiceTest {
    @Mock
    private TemplateWidgetRepository repository;

    @InjectMocks
    private TemplateWidgetService service;

    @Test
    void getTemplateByIdTestReturnsTemplate(){
        Template template1 = new Template();
        template1.setName("template1");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);
        when(repository.findById(1L)).thenReturn(Optional.of(templateWidget1));

        TemplateWidget templateWidget = service.getTemplateWidgetById(1L);

        assertThat(templateWidget.getTemplate()).isEqualTo(template1);
        assertThat(templateWidget.getWidget()).isEqualTo(widget1);
        assertThat(templateWidget.getTop()).isEqualTo(1);
    }

    @Test
    void testSaveTemplateWidget(){
        Template template1 = new Template();
        template1.setName("template1");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);
        when(repository.save(templateWidget1)).thenReturn(templateWidget1);

        TemplateWidget templateWidget = service.saveTemplateWidget(templateWidget1);

        assertThat(templateWidget.getTemplate()).isEqualTo(template1);
        assertThat(templateWidget.getWidget()).isEqualTo(widget1);
        assertThat(templateWidget.getTop()).isEqualTo(1);
    }

    @Test
    void testDeleteTemplateWidget(){
        service.deleteTemplateWidget(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateTemplateWidget(){
        Template template1 = new Template();
        template1.setName("template1");

        Template template2 = new Template();
        template2.setName("template2");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);


        TemplateWidget templateWidget2 = new TemplateWidget();
        templateWidget2.setTemplate(template2);
        templateWidget2.setWidget(widget1);
        templateWidget2.setTop(2);
        templateWidget2.setLeftPosition(2);
        templateWidget2.setWidth(2);
        templateWidget2.setHeight(2);


        when(repository.findById(1L)).thenReturn(Optional.of(templateWidget1));
        when(repository.save(templateWidget1)).thenReturn(templateWidget1);

        TemplateWidget templateWidget = service.updateTemplateWidget(1L, templateWidget2);

        assertThat(templateWidget.getTemplate()).isEqualTo(template2);
        assertThat(templateWidget.getWidget()).isEqualTo(widget1);
        assertThat(templateWidget.getTop()).isEqualTo(2);
    }

    @Test
    void testGetAllTemplateWidgets(){
        Template template1 = new Template();
        template1.setName("template1");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);

        Template template2 = new Template();
        template2.setName("template2");

        Widget widget2 = new Widget();
        widget2.setName("widget2");

        TemplateWidget templateWidget2 = new TemplateWidget();
        templateWidget2.setTemplate(template2);
        templateWidget2.setWidget(widget2);
        templateWidget2.setTop(2);
        templateWidget2.setLeftPosition(2);
        templateWidget2.setWidth(2);
        templateWidget2.setHeight(2);

        List<TemplateWidget> templateWidgets = Arrays.asList(templateWidget1, templateWidget2);
        when(repository.findAll()).thenReturn(templateWidgets);

        Iterable<TemplateWidget> templateWidgetList = service.getAllTemplateWidgets();

        assertThat(templateWidgetList).hasSize(2).contains(templateWidget1, templateWidget2);
    }






}
