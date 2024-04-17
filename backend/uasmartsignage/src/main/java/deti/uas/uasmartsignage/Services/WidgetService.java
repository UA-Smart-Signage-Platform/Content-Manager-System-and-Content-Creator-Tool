package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import deti.uas.uasmartsignage.Repositories.WidgetRepository;
import deti.uas.uasmartsignage.Models.Widget;

@Service
public class WidgetService {

    @Autowired
    private WidgetRepository widgetRepository;

    public WidgetService(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public Widget getWidgetById(Long id) {
        return widgetRepository.findById(id).orElse(null);
    }

    public Widget saveWidget(Widget widget) {
        return widgetRepository.save(widget);
    }

    public void deleteWidget(Long id) {
        widgetRepository.deleteById(id);
    }

    public Widget updateWidget(Long id, Widget widget) {
        Widget widgetById = widgetRepository.findById(id).orElse(null);
        if (widgetById == null) {
            return null;
        }
        widgetById.setName(widget.getName());
        widgetById.setPath(widget.getPath());
        widgetById.setContents(widget.getContents());
        return widgetRepository.save(widgetById);
    }

    public Iterable<Widget> getAllWidgets() {
        return widgetRepository.findAll();
    }

    public Widget getWidgetsByName(String name) {
        return widgetRepository.findByName(name);
    }



}
