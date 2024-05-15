package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int top;

    //left conflict with sql
    @Column(nullable = false)
    private int leftPosition;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @ManyToOne
    @JoinColumn(name = "templateId", nullable = false)
    @JsonIgnoreProperties("templateWidgets")
    private Template template;

    @ManyToOne
    @JoinColumn(name = "widgetId", nullable = false)
    @JsonIgnoreProperties("templateWidgets")
    private Widget widget;
    
    
}
