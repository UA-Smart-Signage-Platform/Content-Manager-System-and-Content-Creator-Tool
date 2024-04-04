package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Repositories.TemplateWidgetRepository;

@Service
public class TemplateWidgetService {

    @Autowired
    private TemplateWidgetRepository templateWidgetRepository;

    public TemplateWidget getTemplateWidgetById(Long id) {
        return templateWidgetRepository.findById(id).orElse(null);
    }

    public TemplateWidget saveTemplateWidget(TemplateWidget templateWidget) {
        return templateWidgetRepository.save(templateWidget);
    }

    public void deleteTemplateWidget(Long id) {
        templateWidgetRepository.deleteById(id);
    }

    public TemplateWidget updateTemplateWidget(Long id, TemplateWidget templateWidget) {
        TemplateWidget templateWidgetById = templateWidgetRepository.findById(id).orElse(null);
        if (templateWidgetById == null) {
            return null;
        }
        templateWidgetById.setTop(templateWidget.getTop());
        templateWidgetById.setLeftPosition(templateWidget.getLeftPosition());
        templateWidgetById.setWidth(templateWidget.getWidth());
        templateWidgetById.setHeight(templateWidget.getHeight());
        templateWidgetById.setWidget(templateWidget.getWidget());
        templateWidgetById.setTemplate(templateWidget.getTemplate());
        return templateWidgetRepository.save(templateWidgetById);
    }

    public Iterable<TemplateWidget> getAllTemplateWidgets() {
        return templateWidgetRepository.findAll();
    }
    
}
