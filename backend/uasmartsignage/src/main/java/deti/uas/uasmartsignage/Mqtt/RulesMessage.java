package deti.uas.uasmartsignage.Mqtt;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesMessage {

    private String method;
    private List<Map<String,Object>> rules;
    
}
