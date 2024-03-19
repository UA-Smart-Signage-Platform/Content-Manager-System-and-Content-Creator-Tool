package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TemplateWidgets")
public class TemplateWidget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long top;

    //left conflict with sql
    @Column(nullable = false)
    private Long leftPosition;

    @Column(nullable = false)
    private Long width;

    @Column(nullable = false)
    private Long height;

    @ManyToOne
    @JoinColumn(name = "templateId", nullable = false)
    @JsonIgnore
    private Template template;

    @ManyToOne
    @JoinColumn(name = "widgetId", nullable = false)
    @JsonIgnore
    private Widget widget;
   
}
