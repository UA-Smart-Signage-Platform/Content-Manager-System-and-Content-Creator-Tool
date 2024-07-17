package pt.ua.deti.uasmartsignage.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import pt.ua.deti.uasmartsignage.models.embedded.Schedule;

@Getter
public class RuleDTO {

    private long groupId;

    @NotBlank
    private String templateId;

    private Schedule schedule;
    
    private Map<Long, Map<String, Object>> chosenValues;
}
