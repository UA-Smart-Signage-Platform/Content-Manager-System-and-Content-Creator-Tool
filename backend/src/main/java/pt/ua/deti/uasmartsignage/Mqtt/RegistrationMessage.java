package pt.ua.deti.uasmartsignage.Mqtt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationMessage {
    private String method;
    private String name;
    private String width;
    private String height;
    private String uuid;

    
}
