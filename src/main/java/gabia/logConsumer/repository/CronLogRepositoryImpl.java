package gabia.logConsumer.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import gabia.logConsumer.entity.CronLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class CronLogRepositoryImpl implements CronLogRepository {

    @Value("${influx.server}")
    private String server;

    @Value("${influx.token}")
    private char[] token;

    @Value("${influx.org}")
    private String org;

    @Value("${influx.bucket}")
    private String bucket;

    private InfluxDBClient influxDBClient;

    @Override
    public CronLog save(CronLog cronLog) {
        influxDBClient = InfluxDBClientFactory.create(server, token, org, bucket);
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writeMeasurement(WritePrecision.MS, cronLog);
        }
        influxDBClient.close();
        return cronLog;
    }
}
