package pt.ua.deti.uasmartsignage.models.embedded;

import pt.ua.deti.uasmartsignage.models.Widget;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class TemplateWidget {

    private static long lastId = 0;

    private long id;
    
    private Widget widget;

    private int top;

    private int left;
    
    private int width;
    
    private int height;
    
    private Map<String, Object> defaultValues = new HashMap<>();

    public TemplateWidget(Widget widget, int top, int left, int width, int height){
        id = ++lastId;
        this.widget = widget;
        setTop(top);
        setLeft(top);
        setWidth(top);
        setHeight(top);
    }

    public void setDefaultValue(String name, Object value){
        defaultValues.put(name, value);
    }

    public void setTop(int top) {
        validatePercentage(top);
        this.top = top;
    }

    public void setLeft(int left) {
        validatePercentage(left);
        this.left = left;
    }

    public void setWidth(int width) {
        validatePercentage(width);
        if (width < widget.getMinWidth()) {
            throw new IllegalArgumentException("width must be greater than minWidth");
        }
        this.width = width;
    }

    public void setHeight(int height) {
        validatePercentage(height);
        if (height < widget.getMinHeight()) {
            throw new IllegalArgumentException("height must be greater than minHeight");
        }
        this.height = height;
    }

    private void validatePercentage(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Position and size values must be a number between 0 and 100");
        }
    }
}