package gabia.logConsumer.service;

import static org.mockito.BDDMockito.given;

import gabia.logConsumer.business.CronLogBusiness;
import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.entity.Enum.NoticeType;
import java.sql.Timestamp;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class CronLogServiceTest {

    @InjectMocks
    CronLogService cronLogService;

    @Mock
    CronLogBusiness cronLogBusiness;

    @Test
    void addCronLog_성공() throws Exception {
        // Given
        String message = "2021-07-07 03:51:00.000 550e8400-e29b-41d4-a716-446655440000 3 Running: (CRON) info (No MTA installed, discarding output)";

        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setPid("3");
        parsedLogDTO.setTimestamp(Timestamp.valueOf("2021-07-07 03:51:00.000"));
        parsedLogDTO.setCronJobId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        parsedLogDTO.setContent("(CRON) info (No MTA installed, discarding output)");
        parsedLogDTO.setNoticeType(NoticeType.Running);

        given(cronLogBusiness.saveLog(parsedLogDTO)).willReturn(parsedLogDTO);
        // When
        ParsedLogDTO result = cronLogService.addCronLog(message);
        // Then
        Assertions.assertThat(parsedLogDTO).isEqualTo(result);
    }
}