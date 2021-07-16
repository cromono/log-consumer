package gabia.logConsumer.service;

import gabia.logConsumer.business.CronLogBusiness;
import gabia.logConsumer.business.NoticeBusiness;
import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.repository.CronLogRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 카프카로 전달받는 로그를 처리하는 클래스입니다.
 */

@RequiredArgsConstructor
@Service
public class CronLogService {

    private final CronLogBusiness cronLogBusiness;
    /**
     * 카프카 메시지를 컨슘하여 InfluxDB에 로그를 저장
     *
     * @param message 카프카 메시지
     * @return 저장된 크론로그 객체
     * @throws IOException
     */
    @KafkaListener(topics = "syslog", groupId = "my-test")
    public ParsedLogDTO addCronLog(String message) throws IOException {

        ParsedLogDTO parsedLogDTO = new ParsedLogDTO().fromMessage(message);

        ParsedLogDTO result = cronLogBusiness.saveLog(parsedLogDTO);

        return result;
    }


}
