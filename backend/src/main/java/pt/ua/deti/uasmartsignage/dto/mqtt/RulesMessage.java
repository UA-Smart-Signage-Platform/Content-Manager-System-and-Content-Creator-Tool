package pt.ua.deti.uasmartsignage.dto.mqtt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.deti.uasmartsignage.dto.mqtt.embedded.MqttRule;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesMessage implements MqttPayload {

    private String method;
    private List<MqttRule> rules = new ArrayList<>();
    private Set<String> files = new HashSet<>();
    
    public void addMqttRule(MqttRule rule){
        rules.add(rule);
    }

    public void addFile(String file){
        files.add(file);
    }
}
