package gabia.logConsumer.business;

import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.dto.WebhookDTO;
import gabia.logConsumer.dto.WebhookDTO.Request;
import gabia.logConsumer.entity.Enum.NoticeType;
import gabia.logConsumer.entity.Enum.WebhookEndpoint;
import gabia.logConsumer.entity.NoticeSubscription;
import gabia.logConsumer.entity.WebhookSubscription;
import gabia.logConsumer.repository.NoticeSubscriptionRepository;
import gabia.logConsumer.repository.WebhookSubscriptionRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
class WebhookBusinessTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WebhookSubscriptionRepository webhookSubscriptionRepository;

    @Mock
    private KafkaTemplate<String, WebhookDTO.Request> kafkaTemplate;

    @Mock
    private NoticeSubscriptionRepository noticeSubscriptionRepository;

    @InjectMocks
    private WebhookBusiness webhookBusiness;

    @Test
    void produceWebhook() {

        //given
        openMocks(this);
        UUID uuid = UUID.randomUUID();

        List<NoticeSubscription> noticeSubscriptionList = new LinkedList<>();
        NoticeSubscription noticeSubscription1 = NoticeSubscription.builder()
            .id(1L)
            .createUserAccount("test")
            .rcvUserAccount("test")
            .cronJobId(uuid)
            .build();
        NoticeSubscription noticeSubscription2 = NoticeSubscription.builder()
            .id(2L)
            .createUserAccount("test2")
            .rcvUserAccount("test2")
            .cronJobId(uuid)
            .build();

        noticeSubscriptionList.add(noticeSubscription1);
        noticeSubscriptionList.add(noticeSubscription2);

        given(noticeSubscriptionRepository.findByCronJobId(uuid))
            .willReturn(noticeSubscriptionList);

        List<Long> noticeSubscriptionIdList = new LinkedList<>();
        noticeSubscriptionIdList.add(1L);
        noticeSubscriptionIdList.add(2L);

        List<WebhookSubscription> webhookSubscriptionList = new LinkedList<>();
        WebhookSubscription webhookSubscription1 = WebhookSubscription.builder()
            .id(1L)
            .noticeSubscription(noticeSubscription1)
            .endpoint(WebhookEndpoint.SLACK)
            .url("http://127.0.0.1:8080/slack")
            .build();
        WebhookSubscription webhookSubscription2 = WebhookSubscription.builder()
            .id(2L)
            .noticeSubscription(noticeSubscription1)
            .endpoint(WebhookEndpoint.HIWORKS)
            .url("http://127.0.0.1:8080/hiworks")
            .build();

        webhookSubscriptionList.add(webhookSubscription1);
        webhookSubscriptionList.add(webhookSubscription2);

        given(webhookSubscriptionRepository.findByNoticeSubscriptionIdIn(noticeSubscriptionIdList))
            .willReturn(webhookSubscriptionList);

        //when

        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setCronJobId(uuid);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setPid("1");
        parsedLogDTO.setTimestamp(Timestamp.from(Instant.now()));

        List<Request> response = webhookBusiness.produceWebhook(parsedLogDTO);

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

        Assertions.assertEquals(response.get(0).getUrl(), "http://127.0.0.1:8080/slack");
        Assertions.assertEquals(response.get(1).getUrl(), "http://127.0.0.1:8080/hiworks");
        Assertions.assertEquals(response.get(0).getText(), text);
    }
}