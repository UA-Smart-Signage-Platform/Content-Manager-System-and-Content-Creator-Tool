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
public class ScheduleMqtt {

    private String startTime;
    private String endTime;
    private List<Integer> weekdays;
    private String startDate;
    private String endDate;
    private Integer priority;
}
