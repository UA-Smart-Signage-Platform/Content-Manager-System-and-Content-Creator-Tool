package deti.uas.uasmartsignage.Controllers;


import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/influx")
public class InfluxController {
    InfluxDB influxDB;

    public InfluxController() {
        this.influxDB = InfluxDBFactory.connect("http://influxdb:8086");
    }

    @GetMapping("/ping")
    public ResponseEntity<?> pingInflux() {
        Pong pong = influxDB.ping();
        return ResponseEntity.ok(pong);
    }


}
