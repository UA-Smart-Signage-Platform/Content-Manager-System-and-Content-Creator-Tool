package pt.ua.deti.uasmartsignage.mqtt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeepAliveMessage {

    private String method;
    private String uuid;
    
}