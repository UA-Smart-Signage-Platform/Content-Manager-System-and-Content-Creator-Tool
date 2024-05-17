package deti.uas.uasmartsignage.Mqtt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateMessage {

    private String method;
    private String html;
    private List<String> files;
    private ScheduleMqtt schedule;
}

