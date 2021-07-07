package gabia.logConsumer.util;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

public class InfluxDBConnector {

    private static final String DB_URL = "http://182.162.142.151:8086/";
    private static final char[] DB_TOKEN = "W90KOru9HsUcsbJ-7NDZvl-ECE5OdsKe3F8LsuNlY5pNQr9mtrE887RnARrNP1Jr6MgE3BACeXptTOkp6E5ibQ=="
        .toCharArray();
    private static final String DB_ORG = "Gabia";
    private static final String DB_BUCKET = "Cron";

    private static InfluxDBConnector instance;
    private InfluxDBClient influxDBClient;

    private InfluxDBConnector() {};

    public static InfluxDBConnector getInstance() {
        synchronized (InfluxDBConnector.class) {
            if (instance == null) {
                instance = new InfluxDBConnector();
                instance.tryToConnect();
            }
        }
        return instance;
    }

    private void tryToConnect() {
        influxDBClient = InfluxDBClientFactory.create(DB_URL, DB_TOKEN, DB_ORG, DB_BUCKET);
    }
}
