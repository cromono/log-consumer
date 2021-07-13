package gabia.logConsumer.business;

import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.dto.WebhookDTO;
import gabia.logConsumer.entity.WebhookSubscription;
import gabia.logConsumer.repository.NoticeSubscriptionRepository;
import gabia.logConsumer.repository.WebhookSubscriptionRepository;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WebhookBusiness {

    private final RestTemplate restTemplate;
    private final WebhookSubscriptionRepository webhookSubscriptionRepository;
    private final KafkaTemplate<String, WebhookDTO.Request> kafkaTemplate;
    private final NoticeSubscriptionRepository noticeSubscriptionRepository;

    /**
     * 웹훅 생성
     *
     * @param parsedLogDTO
     */
    public List<WebhookDTO.Request> produceWebhook(ParsedLogDTO parsedLogDTO) {

        // Post 할 Request 생성
        List<Long> noticeSubscriptionIdList = noticeSubscriptionRepository
            .findByCronJobId(parsedLogDTO.getCronJobId()).stream()
            .map(dto -> dto.getId())
            .collect(Collectors.toList());

        List<WebhookSubscription> webhookSubscriptionList = webhookSubscriptionRepository
            .findByNoticeSubscriptionIdIn(noticeSubscriptionIdList);

        // message 생성
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(parsedLogDTO.getTimestamp());
        sb.append("] ");
        sb.append("(");
        sb.append(parsedLogDTO.getNoticeType().toString());
        sb.append(") ");
        sb.append(parsedLogDTO.getCronJobId());
        sb.append(": ");
        sb.append(parsedLogDTO.getContent());
        String text = sb.toString();

        List<WebhookDTO.Request> result = new LinkedList<>();

        for (WebhookSubscription webhookSubscription : webhookSubscriptionList) {
            WebhookDTO.Request request = new WebhookDTO.Request();
            request.setUrl(webhookSubscription.getUrl());
            request.setText(text);
            result.add(request);
            kafkaTemplate
                .send(webhookSubscription.getEndpoint().toString().toLowerCase(Locale.ROOT),
                    request);
        }

////        DB에 데이터가 존재하지 않아 테스트 용도로 사용, 추후 삭제
//        WebhookDTO.Request request = new WebhookDTO.Request();
//        request.setUrl("https://hooks.hiworks.com/messenger/chat/e475906d6c864b902acd2b9d86fb040d/hiworks/gabia");
//        request.setText(text);
//        kafkaTemplate
//            .send(WebhookEndpoint.HIWORKS.toString().toLowerCase(Locale.ROOT), request);
//
//        WebhookDTO.Request request2 = new WebhookDTO.Request();
//        request2.setUrl("https://hooks.slack.com/services/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");
//        request2.setText(text);
//        kafkaTemplate
//            .send(WebhookEndpoint.SLACK.toString().toLowerCase(Locale.ROOT), request2);

        return result;
    }

    /**
     * Webhook message 전송
     *
     * @param url  webhook을 보낼 곳의 url
     * @param text webhook message (Log) 내용
     */
    public void sendMessage(String url, String text) {
        Map<String, Object> webhookRequest = new HashMap<String, Object>();
        webhookRequest.put("text", text);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(
            webhookRequest);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
