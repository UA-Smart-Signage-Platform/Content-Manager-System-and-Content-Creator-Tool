package deti.uas.uasmartsignage.Controllers;

import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import deti.uas.uasmartsignage.Configuration.InfluxDBProperties;
import deti.uas.uasmartsignage.Models.BackendLog;
import deti.uas.uasmartsignage.Services.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.influxdb.client.InfluxDBClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("api")
public class LogController {

    private final LogsService logsService;

    @Autowired
    public LogController(InfluxDBProperties influxDBProperties, LogsService logsService) {
        this.logsService = logsService;
    }

    @GetMapping("/logs/backend")
    public ResponseEntity<List<BackendLog>> getBackendLogs() {
        List<BackendLog> logs = logsService.getBackendLogs();
        if (logs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(logs);
    }
}


