package gabia.logConsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.logConsumer.business.NoticeBusiness;
import gabia.logConsumer.business.WebhookBusiness;
import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.dto.WebhookDTO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WebhookService {

    private final NoticeBusiness noticeBusiness;
    private final WebhookBusiness webhookBusiness;
    private final ObjectMapper objectMapper;

    /**
     * Notice 생성 Post 및 웹훅 기능
     *
     * @param message Log message
     */
    @KafkaListener(topics = "statusLog", groupId = "log_consumer")
    public void processLog(String message) {

        // 토픽에서 가져온 카프카 메시지 파싱
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO().fromMessage(message);

        // Cron Monitoring 서버에 Notice Post
        String noticeResponse = noticeBusiness.postNotice(parsedLogDTO);

        if (noticeResponse.equals("404")) {
            return;
        }

        // 웹훅 전송
        webhookBusiness.produceWebhook(parsedLogDTO);

        // Influx DB 에 로그 저장
        noticeBusiness.saveLog(parsedLogDTO);
    }

    /**
     * Slack Webhook Listener
     *
     * @param request WebhookDTO.Request
     * @throws IOException
     */
    @KafkaListener(topics = "slack", groupId = "slack_consumer", containerFactory = "webhookListener")
    public void slackConsumer(WebhookDTO.Request request) throws IOException {

        webhookBusiness.sendMessage(request.getUrl(), request.getText());
    }

    /**
     * Hiworks Webhook Listener
     *
     * @param request WebhookDTO.Request
     * @throws IOException
     */
    @KafkaListener(topics = "hiworks", groupId = "hiworks_consumer", containerFactory = "webhookListener")
    public void hiworksConsumer(WebhookDTO.Request request) throws IOException {

        webhookBusiness.sendMessage(request.getUrl(), request.getText());
    }

}
