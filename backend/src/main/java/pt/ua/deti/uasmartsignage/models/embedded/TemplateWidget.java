package pt.ua.deti.uasmartsignage.models.embedded;

import pt.ua.deti.uasmartsignage.models.Widget;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateWidget {

    @DBRef
    private Widget widget;
    private int top;
    private int left;
    private int width;
    private int height;

}