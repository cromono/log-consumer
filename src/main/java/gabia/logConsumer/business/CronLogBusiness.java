package gabia.logConsumer.business;

import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.entity.CronLog;
import gabia.logConsumer.repository.CronLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CronLogBusiness {

    private final CronLogRepository cronLogRepository;

    /**
     * Influx DB에 Log 저장
     *
     * @param parsedLogDTO
     * @return CronLog
     */
    public ParsedLogDTO saveLog(ParsedLogDTO parsedLogDTO) {

        //Log Entity 생성
        CronLog cronLog = CronLog.builder()
            .cronProcess(parsedLogDTO.getPid())
            .log(parsedLogDTO.getContent())
            .logTime(parsedLogDTO.getTimestamp().toInstant())
            .build();

        //Influx DB에 Log 저장
        cronLogRepository.save(cronLog);

        return parsedLogDTO;
    }

}
