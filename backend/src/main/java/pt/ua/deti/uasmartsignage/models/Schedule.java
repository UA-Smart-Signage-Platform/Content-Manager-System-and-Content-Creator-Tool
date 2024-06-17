package pt.ua.deti.uasmartsignage.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import pt.ua.deti.uasmartsignage.mqtt.ScheduleMqtt;
import jakarta.persistence.*;
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
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int frequency;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private int priority;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;

    @ManyToOne
    @JoinColumn(name = "last_edited_by")
    private AppUser lastEditedBy;

    private LocalDate createdOn;

    @OneToMany(mappedBy = "schedule")
    @JsonIgnoreProperties(value={"schedule"},allowSetters = true)
    private List<TemplateGroup> templateGroups;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_weekdays", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "weekdays")
    private List<Integer> weekdays;

    @Override
    public String toString() {
        return "Schedule [createdBy=" + createdBy + ", createdOn=" + createdOn + ", endDate=" + endDate + ", endTime="
                + endTime + ", frequency=" + frequency + ", id=" + id + ", lastEditedBy=" + lastEditedBy + ", priority="
                + priority + ", startDate=" + startDate + ", startTime=" + startTime + ", templateGroups="
                + templateGroups + ", weekdays=" + weekdays + "]";
    }

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