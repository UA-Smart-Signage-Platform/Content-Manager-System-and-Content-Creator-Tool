package pt.ua.deti.uasmartsignage.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TemplateWidgetDTO{

    @NotBlank
    private String widgetId;

    private int top;

    private int left;

    private int width;

    private int height;

    private Map<String, Object> defaultValues;
}