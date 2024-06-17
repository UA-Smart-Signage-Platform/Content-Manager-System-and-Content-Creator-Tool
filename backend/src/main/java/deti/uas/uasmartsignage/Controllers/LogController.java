package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Configuration.InfluxDBProperties;
import deti.uas.uasmartsignage.Models.BackendLog;
import deti.uas.uasmartsignage.Services.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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


