package gabia.logConsumer.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

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

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
class CronLogBusinessTest {

    @Mock
    private CronLogRepository cronLogRepository;

    @InjectMocks
    private CronLogBusiness cronLogBusiness;

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

        ParsedLogDTO parsedLogDTOResult = cronLogBusiness.saveLog(parsedLogDTO);

        // then
        assertThat(parsedLogDTOResult.getContent()).isEqualTo("test");
    }

}
