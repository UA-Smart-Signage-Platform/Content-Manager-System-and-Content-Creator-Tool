package pt.ua.deti.uasmartsignage.Configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "influxdb")
public class InfluxDBProperties {
    private String url;
    private String token;
    private String org;
    private String bucket;
    private String sBucket;
}

