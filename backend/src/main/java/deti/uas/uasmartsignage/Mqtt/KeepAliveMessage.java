package deti.uas.uasmartsignage.Mqtt;

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
