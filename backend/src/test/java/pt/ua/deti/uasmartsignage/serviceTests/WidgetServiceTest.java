package pt.ua.deti.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import pt.ua.deti.uasmartsignage.models.*;
import pt.ua.deti.uasmartsignage.services.LogsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ua.deti.uasmartsignage.repositories.WidgetRepository;
import pt.ua.deti.uasmartsignage.services.WidgetService;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {
    @Mock
    private WidgetRepository repository;

    @Mock
    private LogsService logsService;

    @InjectMocks
    private WidgetService service;

    @Test
    void getWidgetByIdTestReturnsWidget(){
        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        when(repository.findById(1L)).thenReturn(Optional.of(widget));

        Widget widget1 = service.getWidgetById(1L);

        assertThat(widget1.getName()).isEqualTo("widget");

    }

    @Test
    void whenServiceSaveThenRepositorySave(){
        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        when(repository.save(widget)).thenReturn(widget);

        Widget widget1 = service.saveWidget(widget);

        assertThat(widget1.getName()).isEqualTo("widget");
    }

    @Test
    void whenFindAll_thenReturnAllWidgets(){
        Widget widget1 = new Widget();
        widget1.setName("widget1");
        widget1.setPath("path");
        widget1.setContents(List.of(new Content()));

        Widget widget2 = new Widget();
        widget2.setName("widget2");
        widget2.setPath("path");
        widget2.setContents(List.of(new Content()));

        when(repository.findAll()).thenReturn(Arrays.asList(widget1, widget2));

        Iterable<Widget> found = service.getAllWidgets();
        assertThat(found).hasSize(2).extracting(Widget::getName).contains("widget1", "widget2");
    }

    @Test
    void whenDeleteWidget_thenVerifyRepositoryDelete() {
        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        widget.setContents(List.of(new Content()));

        service.deleteWidget(1L);

        verify(repository, times(1)).deleteById(1L);
        assertFalse(repository.existsById(1L));
    }

    @Test
    void whenUpdateWidget_thenReturnUpdatedWidget() {
        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        widget.setContents(List.of(new Content()));
        when(repository.findById(1L)).thenReturn(Optional.of(widget));
        when(repository.save(widget)).thenReturn(widget);

        Widget newwidget = new Widget();
        newwidget.setName("newWidget");
        newwidget.setPath("newPath");
        newwidget.setContents(List.of(new Content()));

        Widget updatedWidget = service.updateWidget(1L, newwidget);

        assertThat(updatedWidget.getName()).isEqualTo("newWidget");
        assertThat(updatedWidget.getPath()).isEqualTo("newPath");
    }

    @Test
    void testGetWidgetByName(){
        Widget widget = new Widget();
        widget.setName("widget");
        widget.setPath("path");
        when(repository.findByName("widget")).thenReturn(widget);

        Widget widget1 = service.getWidgetsByName("widget");

        assertThat(widget1.getName()).isEqualTo("widget");
        assertThat(widget1.getPath()).isEqualTo("path");
    }


}
