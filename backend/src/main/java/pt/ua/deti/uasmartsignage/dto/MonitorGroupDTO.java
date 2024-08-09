package pt.ua.deti.uasmartsignage.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MonitorGroupDTO {

    @NotBlank
    @NotNull
    private String name;

    private String description;
    
    private List<Long> monitorIds;
}
