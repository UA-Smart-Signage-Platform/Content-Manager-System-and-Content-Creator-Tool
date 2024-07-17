package pt.ua.deti.uasmartsignage.models.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pt.ua.deti.uasmartsignage.enums.WidgetVariableType;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class WidgetVariable {

    private String name;

    private WidgetVariableType type;

    private List<String> optionsList;
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        WidgetVariable that = (WidgetVariable) other;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, optionsList);
    }
}