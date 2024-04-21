package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import deti.uas.uasmartsignage.Models.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.uas.uasmartsignage.Repositories.WidgetRepository;
import deti.uas.uasmartsignage.Services.WidgetService;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTest {
    @Mock
    private WidgetRepository repository;

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

    //missing delete and update tests


}
