package gabia.logConsumer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gabia.logConsumer.entity.CronLog;
import gabia.logConsumer.repository.CronLogRepository;
import java.time.Instant;
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
    CronLogRepository cronLogRepository;

    @Test
    void addCronLog_성공() throws Exception {
        // Given
        String message = "{\n"
            + "  \"@timestamp\": \"2021-06-16T06:21:01.681Z\",\n"
            + "  \"@metadata\": {\n"
            + "    \"beat\": \"filebeat\",\n"
            + "    \"type\": \"_doc\",\n"
            + "    \"version\": \"7.13.2\"\n"
            + "  },\n"
            + "  \"agent\": {\n"
            + "    \"id\": \"255f5de9-99b8-4512-bd96-b75d69ddbcc4\",\n"
            + "    \"name\": \"DESKTOP-Q5DGAKN\",\n"
            + "    \"type\": \"filebeat\",\n"
            + "    \"version\": \"7.13.2\",\n"
            + "    \"hostname\": \"DESKTOP-Q5DGAKN\",\n"
            + "    \"ephemeral_id\": \"36319a9d-d2ea-4357-960e-3da320941073\"\n"
            + "  },\n"
            + "  \"ecs\": {\n"
            + "    \"version\": \"1.8.0\"\n"
            + "  },\n"
            + "  \"log\": {\n"
            + "    \"offset\": 93394,\n"
            + "    \"file\": {\n"
            + "      \"path\": \"/var/log/syslog\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"message\": \"Jun 16 15:21:01 DESKTOP-Q5DGAKN CRON[6626]: (CRON) info (No MTA installed, discarding output)\",\n"
            + "  \"input\": {\n"
            + "    \"type\": \"log\"\n"
            + "  },\n"
            + "  \"fields\": {\n"
            + "    \"log_topic\": \"syslog\"\n"
            + "  },\n"
            + "  \"host\": {\n"
            + "    \"ip\": [\n"
            + "      \"172.29.45.55\",\n"
            + "      \"fe80::215:5dff:fee2:2ca4\"\n"
            + "    ],\n"
            + "    \"mac\": [\n"
            + "      \"4e:cb:8b:29:18:5d\",\n"
            + "      \"6e:24:f3:c4:d7:47\",\n"
            + "      \"00:15:5d:e2:2c:a4\"\n"
            + "    ],\n"
            + "    \"hostname\": \"DESKTOP-Q5DGAKN\",\n"
            + "    \"architecture\": \"x86_64\",\n"
            + "    \"os\": {\n"
            + "      \"name\": \"Ubuntu\",\n"
            + "      \"kernel\": \"5.4.72-microsoft-standard-WSL2\",\n"
            + "      \"codename\": \"focal\",\n"
            + "      \"type\": \"linux\",\n"
            + "      \"platform\": \"ubuntu\",\n"
            + "      \"version\": \"20.04.2 LTS (Focal Fossa)\",\n"
            + "      \"family\": \"debian\"\n"
            + "    },\n"
            + "    \"containerized\": false,\n"
            + "    \"name\": \"DESKTOP-Q5DGAKN\"\n"
            + "  }\n"
            + "}\n";
        CronLog cronLog = CronLog.builder()
            .logTime(Instant.parse("2021-06-16T06:21:01.681Z"))
            .log("Jun 16 15:21:01 DESKTOP-Q5DGAKN CRON[6626]: (CRON) info (No MTA installed, discarding output)")
            .cronProcess("6626")
            .build();
        // When
        when(cronLogRepository.save(any())).thenReturn(cronLog);
        CronLog savedLog = cronLogService.addCronLog(message);
        // Then
        Assertions.assertThat(savedLog).isEqualTo(cronLog);
    }
}