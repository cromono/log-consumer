package gabia.logConsumer.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gabia.logConsumer.entity.CronLog;
import gabia.logConsumer.repository.CronLogRepository;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 카프카로 전달받는 로그를 처리하는 클래스입니다.
 */

@RequiredArgsConstructor
@Service
public class CronLogService {


    private final CronLogRepository cronLogRepository;

    /**
     * 카프카 메시지를 컨슘하여 InfluxDB에 로그를 저장
     *
     * @param message 카프카 메시지
     * @return 저장된 크론로그 객체
     * @throws IOException
     */
    @KafkaListener(topics = "syslog", groupId = "my-test")
    public CronLog addCronLog(String message) throws IOException {

        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        Instant logTime = Instant.parse(jsonObject.get("@timestamp").getAsString());
        String log = jsonObject.get("message").getAsString();
        String[] arr = log.split("[\\[,\\],\\s,(,),:]");

        CronLog cronLog = CronLog.builder()
            .logTime(logTime)
            .log(log)
            .cronProcess(arr[7])
            .build();
        // 로그 확인
        System.out.println(cronLog);
        System.out.println(String.format("CRON SYSLOG LOG : %s", log));
        System.out.println(String.format("CRON SYSLOG TIME : %s", logTime));

        return cronLogRepository.save(cronLog);
    }


}
