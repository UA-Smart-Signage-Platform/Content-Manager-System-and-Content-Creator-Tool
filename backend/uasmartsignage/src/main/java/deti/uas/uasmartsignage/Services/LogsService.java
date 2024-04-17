package deti.uas.uasmartsignage.Services;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import deti.uas.uasmartsignage.Configuration.InfluxDBProperties;
import deti.uas.uasmartsignage.Models.BackendLog;
import deti.uas.uasmartsignage.Models.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LogsService {

    private final InfluxDBClient influxDBClient;
    private static final Logger logger = LoggerFactory.getLogger(LogsService.class);

    private final String org;
    private final String bucket;


    public LogsService(InfluxDBProperties influxDBProperties) {
        String token = influxDBProperties.getToken();
        String url = influxDBProperties.getUrl();
        this.org = influxDBProperties.getOrg();
        this.bucket = influxDBProperties.getBucket();
        this.influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray());
    }

    /**
     * Adds a log to the InfluxDB database.
     *
     * @param severity The severity of the log (INFO, WARNING, ERROR).
     * @param operationSource The source of the operation that generated the log.
     * @param operation The operation that generated the log.
     * @param description The description of the log.
     * @return True if the log was successfully added, false otherwise.
     */
    public boolean addBackendLog(Severity severity, String operationSource, String operation, String description) {
        String measurement = "BackendLogs";
        String user = "admin"; // Placeholder for user (will be implemented in future should be retrived from the JWT token)
        try {
            // Prepare the InfluxDB point
            Point point = Point.measurement(measurement)
                    .addTag("operationSource", operationSource)
                    .addField("severity", severity.toString())
                    .addField("user", user)
                    .addField("operation", operation)
                    .addField("description", description)
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            // Write the point to InfluxDB
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(bucket, org, point);
            return true;
        } catch (InfluxException e) {
            logger.error("Failed to add log to InfluxDB: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
        }
        return false;
    }

    public List<BackendLog> getBackendLogs() {
        List<BackendLog> logs = new ArrayList<>();
        // Construct Flux query to retrieve logs
        String fluxQuery = "from(bucket: \""+ bucket +"\")" +
                " |> range(start: -48h)" +  // Adjust time range as needed
                " |> filter(fn: (r) => r._measurement == \"BackendLogs\")";  // Measurement name

        // Execute the query
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, org);

        // Return empty list if no logs are found
        if (tables.isEmpty()) {
            return logs;
        }

        // Initialize list of BackendLog objects
        int size = tables.get(0).getRecords().size();
        for (int i = 0; i < size; i++) {
            BackendLog backendLog = new BackendLog();
            logs.add(backendLog);
        }

        // Process query results
        for (FluxTable table : tables) {
            List<FluxRecord> records = table.getRecords();
            int index = 0;
            // Iterate over records and populate BackendLog objects (this is needed because the query returns multiple fields for each record)
            for (FluxRecord fluxRecord : records) {
                int finalIndex = index;
                fluxRecord.getValues().forEach((k, v) -> {
                    if (k.equals("operationSource")) {
                        logs.get(finalIndex).setModule(v.toString());
                    }
                });
                if (Objects.equals(fluxRecord.getField(), "description")) {
                    logs.get(index).setDescription(Objects.requireNonNull(fluxRecord.getValue()).toString());
                }
                if (Objects.equals(fluxRecord.getField(), "operation")) {
                    logs.get(index).setOperation(Objects.requireNonNull(fluxRecord.getValue()).toString());
                }
                if (Objects.equals(fluxRecord.getField(), "severity")) {
                    logs.get(index).setSeverity(Severity.valueOf(Objects.requireNonNull(fluxRecord.getValue()).toString()));
                }
                if (Objects.equals(fluxRecord.getField(), "user")) {
                    logs.get(index).setUser(Objects.requireNonNull(fluxRecord.getValue()).toString());
                }
                logs.get(index).setTimestamp(Objects.requireNonNull(fluxRecord.getTime()).toString());
                index++;
            }
        }
        return logs;
    }

    public boolean addMonitorLog(String measurement, String operationSource, String operation, String description, String bucket) {
        //not implemented yet
        try {
            // Prepare the InfluxDB point
            Point point = Point.measurement(measurement)
                    .addTag("operationSource", operationSource)
                    .addField("operation", operation)
                    .addField("description", description)
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            // Write the point to InfluxDB
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(bucket, org, point);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
