package pt.ua.deti.uasmartsignage.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TemplateWidgetDTO{

    @NotBlank
    private String widgetId;

    private float top;

    private float left;

    private float width;

    private float height;

    private int zindex;

    private Map<String, Object> defaultValues;
}