package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Shcedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "date")
    private LocalDate _date;

    @Column(name = "priority")
    private int priority;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_edited_by", referencedColumnName = "id")
    private User lastEditedBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    @JsonIgnoreProperties({"schedules", "monitors"})
    private MonitorsGroup monitorsGroupForSchedules;

}