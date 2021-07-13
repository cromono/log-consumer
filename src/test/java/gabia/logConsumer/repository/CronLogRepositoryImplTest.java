package gabia.logConsumer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gabia.logConsumer.entity.CronLog;
import java.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class CronLogRepositoryImplTest {

    @Autowired
    CronLogRepository cronLogRepository;

    @Test
    public void save() {
        // Given
        CronLog cronLog = CronLog.builder()
            .logTime(Instant.now())
            .log("test")
            .cronProcess("test")
            .build();
        // When
        CronLog savedLog = cronLogRepository.save(cronLog);
        // Then
        assertThat(cronLog).isEqualTo(savedLog);
    }
}