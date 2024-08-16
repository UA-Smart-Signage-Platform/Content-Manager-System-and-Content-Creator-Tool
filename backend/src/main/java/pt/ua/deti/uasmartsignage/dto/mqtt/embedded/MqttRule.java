package pt.ua.deti.uasmartsignage.dto.mqtt.embedded;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MqttRule {
    String html;
    MqttSchedule schedule;
}
