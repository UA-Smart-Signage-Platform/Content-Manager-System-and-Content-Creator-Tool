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

    private float top;

    private float left;
    
    private float width;
    
    private float height;

    private int zindex;
    
    private Map<String, Object> defaultValues = new HashMap<>();

    public TemplateWidget(Widget widget, float top, float left, float width, float height, int zindex){
        id = ++lastId;
        this.widget = widget;
        setTop(top);
        setLeft(left);
        setWidth(width);
        setHeight(height);
        this.zindex = zindex;
    }

    public void setDefaultValue(String name, Object value){
        defaultValues.put(name, value);
    }

    public Object getDefaultValue(String variableName){
        if (defaultValues.containsKey(variableName)){
            return defaultValues.get(variableName);
        }
        else {
            return null;
        }
    }

    public void setTop(float top) {
        validatePercentage(top);
        this.top = top;
    }

    public void setLeft(float left) {
        validatePercentage(left);
        this.left = left;
    }

    public void setWidth(float width) {
        validatePercentage(width);
        if (width < widget.getMinWidth()) {
            throw new IllegalArgumentException("width must be greater than minWidth");
        }
        this.width = width;
    }

    public void setHeight(float height) {
        validatePercentage(height);
        if (height < widget.getMinHeight()) {
            throw new IllegalArgumentException("height must be greater than minHeight");
        }
        this.height = height;
    }

    private void validatePercentage(float value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Position and size values must be a number between 0 and 100");
        }
    }
}