package gabia.logConsumer.business;

import gabia.logConsumer.dto.NoticeDTO;
import gabia.logConsumer.dto.NoticeDTO.Request;
import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.entity.CronLog;
import gabia.logConsumer.repository.CronLogRepository;
import gabia.logConsumer.repository.NoticeSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NoticeBusiness {

    private final RestTemplate restTemplate;
    private final CronLogRepository cronLogRepository;

    //    private final String url = "http://10.7.27.11:80/notifications/notice";
    private final String url = "http://localhost:8081/notifications/notice";

    /**
     * Notice 생성
     *
     * @param parsedLogDTO
     * @return NoticeDTO.Response
     */
    public String postNotice(ParsedLogDTO parsedLogDTO) {

        // Post 할 Request 생성
        NoticeDTO.Request request = new Request();
        request.setNoticeMessage(parsedLogDTO.getContent());
        request.setNoticeType(parsedLogDTO.getNoticeType());
        request.setCronJobId(parsedLogDTO.getCronJobId());
        request.setNoticeCreateDateTime(parsedLogDTO.getTimestamp());

        // 생성한 Request Cron Monitoring 서버로 Post
        HttpHeaders headers = new HttpHeaders();
        try {
            String response = restTemplate
                .postForObject(url, new HttpEntity<>(request, headers), String.class);
            return response;
        } catch (HttpClientErrorException e) {
            String errorStatus = Integer.toString(e.getRawStatusCode());
            return errorStatus;
        }
    }

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
