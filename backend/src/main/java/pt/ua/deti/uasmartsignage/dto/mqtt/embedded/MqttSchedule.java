package pt.ua.deti.uasmartsignage.dto.mqtt.embedded;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MqttSchedule {
    public String startTime;
    public String endTime;
    public List<Integer> weekdays;
    public String startDate;
    public String endDate;
    public Integer priority;
}
