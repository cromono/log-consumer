package gabia.logConsumer.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import gabia.logConsumer.dto.NoticeDTO;
import gabia.logConsumer.dto.NoticeDTO.Request;
import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.entity.CronLog;
import gabia.logConsumer.entity.Enum.NoticeType;
import gabia.logConsumer.repository.CronLogRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
class NoticeBusinessTest {

    @Mock
    private CronLogRepository cronLogRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NoticeBusiness noticeBusiness;


    @Test
    void postNotice_성공() {

        // given
        openMocks(this);
        NoticeDTO.Request request = new Request();
        request.setNoticeMessage("test");
        request.setNoticeType(NoticeType.Start);
        request.setCronJobId(UUID.randomUUID());
        request.setNoticeCreateDateTime(Timestamp.from(Instant.now()));

        given(restTemplate.postForObject(any(String.class), any(), any()))
            .willReturn("test");

        // when
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(request.getCronJobId());
        parsedLogDTO.setTimestamp(request.getNoticeCreateDateTime());
        parsedLogDTO.setPid("1");

        String notice = noticeBusiness.postNotice(parsedLogDTO);

        // then
        assertThat(notice).isEqualTo("test");

    }

    @Test
    void postNotice_실패_크론_잡이_존재하지_않는_경우() {

        // given
        openMocks(this);
        NoticeDTO.Request request = new Request();
        request.setNoticeMessage("test");
        request.setNoticeType(NoticeType.Start);
        request.setCronJobId(UUID.randomUUID());
        request.setNoticeCreateDateTime(Timestamp.from(Instant.now()));

        given(restTemplate.postForObject(any(String.class), any(), any()))
            .willThrow(HttpClientErrorException.class);

        // when
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(request.getCronJobId());
        parsedLogDTO.setTimestamp(request.getNoticeCreateDateTime());
        parsedLogDTO.setPid("1");

        String notice = noticeBusiness.postNotice(parsedLogDTO);

        // then
        assertThat(notice).isEqualTo("0");

    }

    @Test
    void saveLog() {

        // given
        openMocks(this);
        CronLog cronLog = CronLog.builder()
            .logTime(Instant.now())
            .log("test")
            .cronProcess("1")
            .build();

        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setPid("1");
        parsedLogDTO.setContent("test");
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setTimestamp(Timestamp.from(cronLog.getLogTime()));
        parsedLogDTO.setCronJobId(UUID.randomUUID());

        given(cronLogRepository.save(any())).willAnswer(returnsFirstArg());

        // when

        ParsedLogDTO parsedLogDTOResult = noticeBusiness.saveLog(parsedLogDTO);

        // then
        assertThat(parsedLogDTOResult.getContent()).isEqualTo("test");
    }
}