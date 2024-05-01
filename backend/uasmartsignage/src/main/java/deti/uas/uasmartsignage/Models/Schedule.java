package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String frequency;

    private int nTimes;

    private int intervalOfTime;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDate date;

    private int priority;

    @ElementCollection
    @CollectionTable(name = "schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "day")
    private List<Integer> days;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "last_edited_by")
    private User lastEditedBy;

    private LocalDate createdOn;

    @OneToMany(mappedBy = "schedule")
    @JsonIgnoreProperties("schedule")
    private List<TemplateGroup> templateGroups;


    @Override
    public String toString() {
        return "Schedule [id=" + id + ", frequency=" + frequency + ", nTimes=" + nTimes + ", intervalOfTime=" + intervalOfTime + ", startDate=" + startDate + ", endDate=" + endDate + ", date=" + date + ", priority=" + priority + ", days=" + days + ", createdBy=" + createdBy + ", lastEditedBy=" + lastEditedBy + ", createdOn=" + createdOn + "]";
    }
}