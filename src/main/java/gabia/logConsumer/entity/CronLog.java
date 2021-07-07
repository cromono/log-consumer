package gabia.logConsumer.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Measurement(name = "cron_log")
@Builder
public class CronLog {

    @Column(timestamp = true)
    private Instant logTime;

    @Column(tag = true)
    private String cronProcess;

    @Column
    private String log;
}