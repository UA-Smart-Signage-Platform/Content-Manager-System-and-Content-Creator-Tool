package pt.ua.deti.uasmartsignage.models.embedded;

import pt.ua.deti.uasmartsignage.dto.mqtt.embedded.MqttSchedule;
import pt.ua.deti.uasmartsignage.models.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> weekdays;
    private int priority;

    private AppUser createdBy;
    private AppUser lastEditedBy;
    private LocalDate createdOn;

    // TODO: make sure to test if values are null or empty
    // and decide on what to send (update documentation)
    public MqttSchedule toMqttFormat(){
        MqttSchedule mqttSchedule = new MqttSchedule();
        mqttSchedule.startTime = (startTime != null) ? startTime.toString() : "00:01";
        mqttSchedule.endTime = (endTime != null) ? endTime.toString() : "23:59";
        mqttSchedule.startDate = (startDate != null) ? startDate.toString() : "";
        mqttSchedule.endDate = (endDate != null) ? endDate.toString() : "";
        mqttSchedule.weekdays = weekdays;
        mqttSchedule.priority = priority;
        return mqttSchedule;
    }
}