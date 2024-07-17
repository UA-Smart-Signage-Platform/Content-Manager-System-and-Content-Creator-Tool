package pt.ua.deti.uasmartsignage.models.embedded;

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
}