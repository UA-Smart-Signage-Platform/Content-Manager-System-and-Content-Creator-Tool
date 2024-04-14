package deti.uas.uasmartsignage.Controllers;

import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import deti.uas.uasmartsignage.Configuration.InfluxDBProperties;
import deti.uas.uasmartsignage.Models.BackendLog;
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

    private final InfluxDBClient influxDBClient;

    private String org;

    @Autowired
    public LogController(InfluxDBProperties influxDBProperties) {
        this.influxDBClient = InfluxDBClientFactory.create(influxDBProperties.getUrl(), influxDBProperties.getToken().toCharArray());
        this.org = influxDBProperties.getOrg();
    }

    @GetMapping("/logs/backend")
    public ResponseEntity<List<BackendLog>> getBackendLogs() {
        List<BackendLog> logs = new ArrayList<>();
        try {
            String bucket = "changeme";

            // Construct Flux query to retrieve logs
            String fluxQuery = "from(bucket: \""+ bucket +"\")" +
                    " |> range(start: -48h)" +  // Adjust time range as needed
                    " |> filter(fn: (r) => r._measurement == \"BackendLogs\")";  // Measurement name


            // Execute the query
            QueryApi queryApi = influxDBClient.getQueryApi();
            List<FluxTable> tables = queryApi.query(fluxQuery, org);

            int size = tables.get(0).getRecords().size();
            for (int i = 0; i < size; i++) {
                BackendLog backendLog = new BackendLog();
                logs.add(backendLog);
            }

            // Process query results
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                int index = 0;
                for (FluxRecord record : records) {
                    int finalIndex = index;
                    record.getValues().forEach((k, v) -> {
                        if (k.equals("operationSource")) {
                            logs.get(finalIndex).setModule(v.toString());
                        }
                    });
                    if (Objects.equals(record.getField(), "description")) {
                        logs.get(index).setDescription(Objects.requireNonNull(record.getValue()).toString());
                    }
                    if (Objects.equals(record.getField(), "operation")) {
                        logs.get(index).setOperation(Objects.requireNonNull(record.getValue()).toString());
                    }
                    if (Objects.equals(record.getField(), "severity")) {
                        logs.get(index).setSeverity(Objects.requireNonNull(record.getValue()).toString());
                    }
                    logs.get(index).setTimestamp(Objects.requireNonNull(record.getTime()).toString());
                    index++;
                }
            }

            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}


