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
     * Adds a backend log entry to the InfluxDB database.
     *
     * This method creates a log with the specified severity, operation source, 
     * operation name, and description, then writes it under the "BackendLogs" measurement.
     *
     * @param severity The severity level of the log (e.g., INFO, WARNING, ERROR).
     * @param operationSource The source of the operation generating the log.
     * @param operation The operation that triggered the log.
     * @param description A description of the log entry.
     * @return {@code true} if the log was added successfully; {@code false} otherwise.
    */
    public boolean addBackendLog(Severity severity, String operationSource, String operation, String description) {
        String user = "admin"; 
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

    /**
     * Retrieves a list of backend logs from InfluxDB for the specified time range.
     * 
     * This method fetches all backend logs recorded within the last specified number 
     * of hours, filtering by the measurement name "BackendLogs". The logs are 
     * aggregated by timestamp and populated into {@link BackendLog} objects, with 
     * special handling for the "Severity" field.
     *
     * @param hours The number of hours to look back for logs (must be positive).
     * @return A list of {@link BackendLog} objects; returns an empty list if no logs 
     *         are found.
     *
     * @throws IllegalArgumentException if hours is negative.
    */
    public List<BackendLog> getBackendLogs(int hours) {
        String query = String.format(
            "from(bucket: \"%s\") " +
            "|> range(start: -%dh) " +
            "|> filter(fn: (r) => r._measurement == \"BackendLogs\")",
            backendBucket, hours
        );        

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query, org);
        Map<String, BackendLog> logs = new HashMap<>();
        
        // The following code is using influxDB queryAPI (v2 OSS).
        // In short, the following code, after having processed the query, will go through each record,
        // aggregate them based on timestamp and populate objects as needed.
        // Due to Severity being a tag (index) it requires special treatment
        for (FluxTable table : tables) {
            for (FluxRecord fluxRecord : table.getRecords()) {
                String timestamp = Objects.requireNonNull(fluxRecord.getTime()).toString();
                BackendLog backendLog = logs.getOrDefault(timestamp, new BackendLog());
                backendLog.setTimestamp(timestamp);

                fluxRecord.getValues().entrySet().stream()
                           .filter(entry -> SEVERITY.equals(entry.getKey()))
                           .findFirst()
                           .ifPresent(entry -> backendLog.setSeverity(entry.getValue() != null ? Severity.valueOf(entry.getValue().toString()) : null));

                switch (Objects.requireNonNull(fluxRecord.getField())) {
                    case OPERATION_SOURCE:
                        backendLog.setModule(Objects.requireNonNull(fluxRecord.getValue()).toString());
                        break;
                    case DESCRIPTION:
                        backendLog.setDescription(Objects.requireNonNull(fluxRecord.getValue()).toString());
                        break;
                    case OPERATION:
                        backendLog.setOperation(Objects.requireNonNull(fluxRecord.getValue()).toString());
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

    /**
     * Retrieves the daily count of backend logs for the last 30 days.
     *
     * This method queries the InfluxDB database to count the number of backend logs 
     * recorded each day over the past 30 days. The results are aggregated by day.
     *
     * @return A list of integers representing the count of logs per day for the last 30 days.
     *         If no logs are found, the list will be empty.
    */
    public List<Integer> getBackendLogsByNumberDaysAndSeverity(Integer days, Severity severity) {
        String query = String.format(
            "from(bucket: \"%s\") " +
            "|> range(start: -%dd) " +
            "|> filter(fn: (r) => r._measurement == \"BackendLogs\") " +
            "|> filter(fn: (r) => r.severity == \"%s\") " + 
            "|> aggregateWindow(every: 1d, fn: count, createEmpty: true) " +
            "|> yield(name: \"logs_per_day\")",
            backendBucket, days, severity.toString()
        );
    
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query, org);
        List<Integer> logsPerDay = new ArrayList<>();


        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                // Extract the _value and cast it to Integer, then add to the list
                Number value = (Number) record.getValueByKey("_value");
                if (value != null) {
                    logsPerDay.add(value.intValue());
                }
            }
        }
    
        if (logsPerDay.size() == 0){
            for (int i = 0; i < days + 1; i++) {
                logsPerDay.add(0);
            }
        }

        return logsPerDay;
    }
    
    public Map<String, Integer> getBackendLogsNumberPerOperationByNumberDaysAndSeverity(Integer days, Severity severity){
        String query = String.format(
            "from(bucket: \"%s\") " +
            "|> range(start: -%dd) " +
            "|> filter(fn: (r) => r._measurement == \"BackendLogs\") " +
            "|> filter(fn: (r) => r.severity == \"%s\", onEmpty: \"keep\") " + 
            "|> group(columns: [\"_time\"]) ",
            backendBucket, days, severity.toString()
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query, org);
        
        Map<String, Integer> logsPerOperationSource = new HashMap<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String field = (String) record.getField();
                if (Objects.requireNonNull(field).equals(OPERATION_SOURCE)){
                    String operationSource = Objects.requireNonNull(record.getValue()).toString();
                    logsPerOperationSource.put(operationSource, logsPerOperationSource.get(operationSource) == null ? 1 : logsPerOperationSource.get(operationSource).intValue() + 1);
                    break;
                }
            }
        }
        return logsPerOperationSource;
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

        String fluxQuery = String.format(
            "from(bucket: \"%s\") " +
            "|> range(start: -10m) " +
            "|> filter(fn: (r) => r._measurement == \"KeepAliveLogs\") " +
            "|> filter(fn: (r) => r.SourceMonitor == \"%s\")",
            monitorBucket, monitorUUID
        );
        
    
    
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

    /**
     * Attempts to add a log entry to InfluxDB and logs an error if it fails.
     *
     * @param severity The log severity (INFO, WARNING, ERROR).
     * @param source The source of the log (class that called it).
     * @param operation The operation associated with the log (method called on).
     * @param description A description of the log entry.
     * @param loggerFromClass Logger for error reporting on failure.
    */
    public void addLogEntry(Severity severity, String source, String operation, String description, Logger loggerFromClass) {
        if (!addBackendLog(severity, source, operation, description)) {
            loggerFromClass.error("Failed to add log to influxDB");
        }
    }
}
