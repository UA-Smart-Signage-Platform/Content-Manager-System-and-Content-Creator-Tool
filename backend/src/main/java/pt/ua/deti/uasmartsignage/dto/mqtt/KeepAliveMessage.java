package pt.ua.deti.uasmartsignage.dto.mqtt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeepAliveMessage implements MqttPayload {

    private String method;
    private String uuid;
    
}
