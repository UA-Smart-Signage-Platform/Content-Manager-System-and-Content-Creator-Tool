package deti.uas.uasmartsignage.Services;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import deti.uas.uasmartsignage.Configuration.InfluxDBProperties;
import org.springframework.stereotype.Service;

@Service
public class LogsService {

    private final InfluxDBClient influxDBClient;

    private String org;


    public LogsService(InfluxDBProperties influxDBProperties) {
        this.influxDBClient = InfluxDBClientFactory.create(influxDBProperties.getUrl(), influxDBProperties.getToken().toCharArray());
        this.org = influxDBProperties.getOrg();
    }

    /**
     * Adds a log to the InfluxDB database.
     *
     * @param measurement The name of the measurement to log.
     * @param severity The severity of the log. 0 for ERROR, 1 for WARNING, 2 for INFO.
     * @param operationSource The source of the operation that generated the log.
     * @param operation The operation that generated the log.
     * @param description The description of the log.
     * @param bucket The bucket to write the log to.
     * @return True if the log was successfully added, false otherwise.
     */
    public boolean addLog(String measurement,Integer severity, String operationSource, String operation, String description, String bucket) {
        String severityString = "INFO";
        if (severity < 0 || severity > 2) {
            return false;
        }
        if (severity == 0) {
            severityString = "ERROR";
        } else if (severity == 1) {
            severityString = "WARNING";
        }
        try {
            // Prepare the InfluxDB point
            Point point = Point.measurement(measurement)
                    .addTag("operationSource", operationSource)
                    .addField("severity", severityString)
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
