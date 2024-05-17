package deti.uas.uasmartsignage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
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
    private float top;

    //left conflict with sql
    @Column(nullable = false)
    private float leftPosition;

    @Column(nullable = false)
    private float width;

    @Column(nullable = false)
    private float height;

    @Column(nullable = false, name="zIndex")
    private int zIndex;

    @ManyToOne
    @JoinColumn(name = "templateId", nullable = false)
    @JsonIgnoreProperties("templateWidgets")
    private Template template;

    @ManyToOne
    @JoinColumn(name = "widgetId", nullable = false)
    @JsonIgnoreProperties("templateWidgets")
    private Widget widget;
    
    public static class ZIndexComparator implements Comparator<TemplateWidget> {
        @Override
        public int compare(TemplateWidget tw1, TemplateWidget tw2) {
            return Integer.compare(tw1.getZIndex(), tw2.getZIndex());
        }
    }
}
