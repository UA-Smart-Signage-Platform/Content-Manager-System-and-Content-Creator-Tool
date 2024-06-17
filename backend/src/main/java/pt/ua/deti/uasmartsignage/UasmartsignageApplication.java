package pt.ua.deti.uasmartsignage;


import pt.ua.deti.uasmartsignage.Configuration.InfluxDBProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(InfluxDBProperties.class)
public class UasmartsignageApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(UasmartsignageApplication.class, args);
    }


}
