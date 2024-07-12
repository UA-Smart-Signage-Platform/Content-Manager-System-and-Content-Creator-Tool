package pt.ua.deti.uasmartsignage.models.embedded;

import pt.ua.deti.uasmartsignage.models.AppUser;
import pt.ua.deti.uasmartsignage.mqtt.ScheduleMqtt;
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

    private int frequency;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private int priority;
    private List<Integer> weekdays;

    private AppUser createdBy;
    private AppUser lastEditedBy;
    private LocalDate createdOn;

    public ScheduleMqtt toMqttFormat() {
        String stringStartDate = "";
        String stringEndDate = "";
        if(startDate != null)
            stringStartDate = startDate.toString();
        if(endDate != null)
            stringEndDate = endDate.toString();
        return new ScheduleMqtt(startTime.toString(), endTime.toString(), weekdays, stringStartDate, stringEndDate, priority);
    }

}