package pt.ua.deti.uasmartsignage.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TemplateDTO {

    @NotBlank
    private String name;

    private List<TemplateWidgetDTO> widgets;
}
