package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private MonitorsGroup monitorsGroupForSchedules;

    @Column(name = "frequency")
    private int frequency;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "priority")
    private int priority;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @ManyToOne
    @JoinColumn(name = "last_edited_by")
    private User lastEditedBy;

    @ElementCollection
    @CollectionTable(name = "schedule_weekdays", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "weekdays")
    private List<Integer> weekdays;

}