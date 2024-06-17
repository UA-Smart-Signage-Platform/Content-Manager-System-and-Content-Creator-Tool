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
import deti.uas.uasmartsignage.Models.Monitor;
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
    private final String backendBucket;
    private final String monitorBucket;

    private static final String SEVERITY = "severity";
    private static final String OPERATION = "operation";
    private static final String DESCRIPTION = "description";
    private static final String OPERATION_SOURCE = "operationSource";
    private static final String MONITOR = "SourceMonitor";
    private static final String USER = "user";

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String UNEXPECTEDERROR = "An unexpected error occurred: {}";


    public LogsService(InfluxDBProperties influxDBProperties) {
        String token = influxDBProperties.getToken();
        String url = influxDBProperties.getUrl();
        this.org = influxDBProperties.getOrg();
        this.backendBucket = influxDBProperties.getBucket();
        this.monitorBucket = influxDBProperties.getSBucket();
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
                    .addTag(OPERATION_SOURCE, operationSource)
                    .addField(SEVERITY, severity.toString())
                    .addField(USER, user)
                    .addField(OPERATION, operation)
                    .addField(DESCRIPTION, description)
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            // Write the point to InfluxDB
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(backendBucket, org, point);
            return true;
        } catch (InfluxException e) {
            logger.error(ADDLOGERROR, e.getMessage());
        } catch (Exception e) {
            logger.error(UNEXPECTEDERROR, e.getMessage());
        }
        return false;
    }

    public List<BackendLog> getBackendLogs() {
        List<BackendLog> logs = new ArrayList<>();
        // Construct Flux query to retrieve logs
        String fluxQuery = "from(bucket: \""+ backendBucket +"\")" +
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
                    if (k.equals(OPERATION_SOURCE)) {
                        logs.get(finalIndex).setModule(v.toString());
                    }
                });
                if (Objects.equals(fluxRecord.getField(), DESCRIPTION)) {
                    logs.get(index).setDescription(Objects.requireNonNull(fluxRecord.getValue()).toString());
                }
                if (Objects.equals(fluxRecord.getField(), OPERATION)) {
                    logs.get(index).setOperation(Objects.requireNonNull(fluxRecord.getValue()).toString());
                }
                if (Objects.equals(fluxRecord.getField(), SEVERITY)) {
                    logs.get(index).setSeverity(Severity.valueOf(Objects.requireNonNull(fluxRecord.getValue()).toString()));
                }
                if (Objects.equals(fluxRecord.getField(), USER)) {
                    logs.get(index).setUser(Objects.requireNonNull(fluxRecord.getValue()).toString());
                }
                logs.get(index).setTimestamp(Objects.requireNonNull(fluxRecord.getTime()).toString());
                index++;
            }
        }
        return logs;
    }

    public boolean addKeepAliveLog(Severity severity, String monitor, String operation) {
        String measurement = "KeepAliveLogs";
        try {
            Point point = Point.measurement(measurement)
                    .addTag(MONITOR, monitor)
                    .addField(SEVERITY, severity.toString())
                    .addField(OPERATION, operation)
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(monitorBucket, org, point);
            return true;
        } catch (InfluxException e) {
            logger.error(ADDLOGERROR, e.getMessage());
        } catch (Exception e) {
            logger.error(UNEXPECTEDERROR, e.getMessage());
        }
        return false;
    }


    public boolean keepAliveIn10min(Monitor monitor) {
        String monitorUUID = monitor.getUuid();

        String fluxQuery = "from(bucket: \""+ monitorBucket +"\")" +
                " |> range(start: -10m)" +  // Adjust time range as needed
                " |> filter(fn: (r) => r._measurement == \"KeepAliveLogs\")" +  
                " |> filter(fn: (r) => r.SourceMonitor == \"" + monitorUUID + "\")";
    
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, org);

        return !tables.isEmpty();
    }
    

    // Not implemented neither in MP or Frontend
    public boolean addMonitorLog(Severity severity, String uuid, String operation, String description, Long time) {
        String measurement = "MonitorLogs";
        try {
            // Prepare the InfluxDB point
            Point point = Point.measurement(measurement)
                    .addTag(MONITOR, uuid)
                    .addField(SEVERITY, severity.toString())
                    .addField(OPERATION, operation)
                    .addField(DESCRIPTION, description)
                    .time(time, WritePrecision.MS); //ask mp to send it in long (miliseconds)

            // Write the point to InfluxDB
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(monitorBucket, org, point);
            return true;
        } catch (InfluxException e) {
            logger.error(ADDLOGERROR, e.getMessage());
        } catch (Exception e) {
            logger.error(UNEXPECTEDERROR, e.getMessage());
        }
        return false;
    }
}
