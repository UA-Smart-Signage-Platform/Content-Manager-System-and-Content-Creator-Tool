package pt.ua.deti.uasmartsignage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.repositories.WidgetRepository;
import pt.ua.deti.uasmartsignage.models.Widget;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository widgetRepository;

    public List<Widget> getAllWidgets() {
        return widgetRepository.findAll();
    }

    public Widget getWidgetById(String id) {
        return widgetRepository.findById(id).orElse(null);
    }

}
