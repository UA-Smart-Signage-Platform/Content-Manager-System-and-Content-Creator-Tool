package pt.ua.deti.uasmartsignage.services;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import pt.ua.deti.uasmartsignage.configuration.InfluxDBProperties;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.embedded.BackendLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

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
        String user = "admin"; // Placeholder for user (will be implemented in future should be retrived from the JWT token)
        try {
            Point point = Point.measurement("BackendLogs")
                    .addTag(SEVERITY, severity.toString())
                    .addField(OPERATION_SOURCE, operationSource)
                    .addField(USER, user)
                    .addField(OPERATION, operation)
                    .addField(DESCRIPTION, description)
                    .time(System.currentTimeMillis(), WritePrecision.MS);

            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(backendBucket, org, point);
            return true;
        } 
        catch (InfluxException e) {
            logger.error(ADDLOGERROR, e.getMessage());
        } 
        catch (Exception e) {
            logger.error(UNEXPECTEDERROR, e.getMessage());
        }
        return false;
    }

    public List<BackendLog> getBackendLogs(int hours) {
        String query = String.format(
            "from(bucket: \"%s\") |> range(start: -%dh) |> filter(fn: (r) => r._measurement == \"BackendLogs\")",
            backendBucket, hours
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query, org);
        Map<String, BackendLog> logs = new HashMap<>();

        for (FluxTable table : tables) {
            for (FluxRecord fluxRecord : table.getRecords()) {
                String timestamp = Objects.requireNonNull(fluxRecord.getTime()).toString();
                BackendLog backendLog = logs.getOrDefault(timestamp, new BackendLog());
                backendLog.setTimestamp(timestamp);
    
                fluxRecord.getValues().entrySet().stream()
                            .filter(entry -> OPERATION_SOURCE.equals(entry.getKey()))
                            .findFirst()
                            .ifPresent(entry -> backendLog.setModule(entry.getValue() != null ? entry.getValue().toString() : null));

                switch (Objects.requireNonNull(fluxRecord.getField())) {
                    case DESCRIPTION:
                        backendLog.setDescription(Objects.requireNonNull(fluxRecord.getValue()).toString());
                        break;
                    case OPERATION:
                        backendLog.setOperation(Objects.requireNonNull(fluxRecord.getValue()).toString());
                        break;
                    case SEVERITY:
                        backendLog.setSeverity(Severity.valueOf(Objects.requireNonNull(fluxRecord.getValue()).toString()));
                        break;
                    case USER:
                        backendLog.setUser(Objects.requireNonNull(fluxRecord.getValue()).toString());
                        break;
                    default:
                        break;
                }
                logs.put(timestamp, backendLog);
            }
        }
        return new ArrayList<>(logs.values());
    }

    public List<Integer> getBackendLogsCountPerDayLast30Days(){
        String query = String.format(
            "from(bucket: \"%s\") |> range(start: -30d) |> filter(fn: (r) => r._measurement == \"BackendLogs\") |> group() |> aggregateWindow(every: 1d, fn: count) |> yield(name: \"logs_per_day\")",
            backendBucket
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query, org);
        List<Integer> logsPerDay = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Long countValue = (Long) record.getValueByKey("_value");
                logsPerDay.add(countValue.intValue());
            }
        }
        return logsPerDay;
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
        } 
        catch (InfluxException e) {
            logger.error(ADDLOGERROR, e.getMessage());
        } 
        catch (Exception e) {
            logger.error(UNEXPECTEDERROR, e.getMessage());
        }
        return false;
    }


    public boolean keepAliveIn10min(Monitor monitor) {
        String monitorUUID = monitor.getUuid();

        String fluxQuery = "from(bucket: \""+ monitorBucket +"\")" +
                " |> range(start: -10m)" + 
                " |> filter(fn: (r) => r._measurement == \"KeepAliveLogs\")" +  
                " |> filter(fn: (r) => r.SourceMonitor == \"" + monitorUUID + "\")";
    
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, org);

        return !tables.isEmpty();
    }
    

    // Not implemented neither in MP or Frontend
    public boolean addMonitorLog(Severity severity, String uuid, String operation, String description, Long time) {
        try {
            // Prepare the InfluxDB point
            Point point = Point.measurement("MonitorLogs")
                    .addTag(MONITOR, uuid)
                    .addField(SEVERITY, severity.toString())
                    .addField(OPERATION, operation)
                    .addField(DESCRIPTION, description)
                    .time(time, WritePrecision.MS); //ask mp to send it in long (miliseconds)

            // Write the point to InfluxDB
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(monitorBucket, org, point);
            return true;
        } 
        catch (InfluxException e) {
            logger.error(ADDLOGERROR, e.getMessage());
        } 
        catch (Exception e) {
            logger.error(UNEXPECTEDERROR, e.getMessage());
        }
        return false;
    }


    public void addLogEntry(Severity severity, String source, String operation, String description, Logger loggerFromClass) {
        if (!addBackendLog(severity, source, operation, description)) {
            loggerFromClass.error("Failed to add log to influxDB");
        }
    }
}
