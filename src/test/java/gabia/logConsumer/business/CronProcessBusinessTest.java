package gabia.logConsumer.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import gabia.logConsumer.dto.ParsedLogDTO;
import gabia.logConsumer.entity.CronJob;
import gabia.logConsumer.entity.Enum.NoticeType;
import gabia.logConsumer.exception.CronJobNotFoundException;
import gabia.logConsumer.repository.CronJobRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
class CronProcessBusinessTest {

    @Mock
    CronJobRepository cronJobRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    CronProcessBusiness cronProcessBusiness;

    @Test
    void postCronProcess_POST_성공() {

        // given
        openMocks(this);
        UUID uuid = UUID.randomUUID();

        CronJob cronJob = CronJob.builder()
            .cronExpr("test")
            .cronName("test")
            .id(uuid)
            .server("127.0.0.1")
            .build();

        given(cronJobRepository.findById(uuid)).willReturn(Optional.of(cronJob));

        Timestamp timestamp = Timestamp.from(Instant.now());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("pid", "1");
        request.put("startTime", timestamp);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

//        String url = String.format("http://localhost:8081/cron-servers/%s/cron-jobs/%s/procss/%s",
//            cronJob.getServer(), cronJob.getId().toString(), "1");
        String url = String.format("http://10.7.27.11:80/cron-servers/%s/cron-jobs/%s/procss/%s",
            cronJob.getServer(), cronJob.getId().toString(), "1");

        Mockito.when(
            restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(uuid);
        parsedLogDTO.setTimestamp(timestamp);
        parsedLogDTO.setPid("1");

        String cronProcess = cronProcessBusiness.postCronProcess(parsedLogDTO);

        // then
        Assertions.assertEquals(cronProcess, "200");
    }

    @Test
    void postCronProcess_PATCH_성공() {

        // given
        openMocks(this);
        UUID uuid = UUID.randomUUID();

        CronJob cronJob = CronJob.builder()
            .cronExpr("test")
            .cronName("test")
            .id(uuid)
            .server("127.0.0.1")
            .build();

        given(cronJobRepository.findById(uuid)).willReturn(Optional.of(cronJob));

        Timestamp timestamp = Timestamp.from(Instant.now());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("pid", "1");
        request.put("endTime", timestamp);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

//        String url = String.format("http://localhost:8081/cron-servers/%s/cron-jobs/%s/procss/%s",
//            cronJob.getServer(), cronJob.getId().toString(), "1");
        String url = String.format("http://10.7.27.11:80/cron-servers/%s/cron-jobs/%s/procss/%s",
            cronJob.getServer(), cronJob.getId().toString(), "1");

        Mockito.when(
            restTemplate.exchange(
                url, HttpMethod.PATCH, entity, String.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.End);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(uuid);
        parsedLogDTO.setTimestamp(timestamp);
        parsedLogDTO.setPid("1");

        String cronProcess = cronProcessBusiness.postCronProcess(parsedLogDTO);

        // then
        Assertions.assertEquals(cronProcess, "200");
    }

    @Test
    void postCronProcess_크론잡이_없는_경우() {

        // given
        openMocks(this);
        UUID uuid = UUID.randomUUID();

        CronJob cronJob = CronJob.builder()
            .cronExpr("test")
            .cronName("test")
            .id(uuid)
            .server("127.0.0.1")
            .build();

        given(cronJobRepository.findById(uuid)).willReturn(Optional.empty());

        Timestamp timestamp = Timestamp.from(Instant.now());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("pid", "1");
        request.put("startTime", timestamp);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

//        String url = String.format("http://localhost:8081/cron-servers/%s/cron-jobs/%s/procss/%s",
//            cronJob.getServer(), cronJob.getId().toString(), "1");
        String url = String.format("http://10.7.27.11:80/cron-servers/%s/cron-jobs/%s/procss/%s",
            cronJob.getServer(), cronJob.getId().toString(), "1");

        Mockito.when(
            restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // when
        // then
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(uuid);
        parsedLogDTO.setTimestamp(timestamp);
        parsedLogDTO.setPid("1");

        assertThrows(CronJobNotFoundException.class, () -> {
            cronProcessBusiness.postCronProcess(parsedLogDTO);
        });
    }

    @Test
    void postCronProcess_POST_실패() {

        // given
        openMocks(this);
        UUID uuid = UUID.randomUUID();

        CronJob cronJob = CronJob.builder()
            .cronExpr("test")
            .cronName("test")
            .id(uuid)
            .server("127.0.0.1")
            .build();

        given(cronJobRepository.findById(uuid)).willReturn(Optional.of(cronJob));

        Timestamp timestamp = Timestamp.from(Instant.now());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("pid", "1");
        request.put("startTime", timestamp);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

//        String url = String.format("http://localhost:8081/cron-servers/%s/cron-jobs/%s/procss/%s",
//            cronJob.getServer(), cronJob.getId().toString(), "1");
        String url = String.format("http://10.7.27.11:80/cron-servers/%s/cron-jobs/%s/procss/%s",
            cronJob.getServer(), cronJob.getId().toString(), "1");

        Mockito.when(
            restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // when
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.Start);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(uuid);
        parsedLogDTO.setTimestamp(timestamp);
        parsedLogDTO.setPid("1");

        String cronProcess = cronProcessBusiness.postCronProcess(parsedLogDTO);

        // then
        Assertions.assertEquals(cronProcess, "404");
    }

    @Test
    void postCronProcess_PATCH_실패() {

        // given
        openMocks(this);
        UUID uuid = UUID.randomUUID();

        CronJob cronJob = CronJob.builder()
            .cronExpr("test")
            .cronName("test")
            .id(uuid)
            .server("127.0.0.1")
            .build();

        given(cronJobRepository.findById(uuid)).willReturn(Optional.of(cronJob));

        Timestamp timestamp = Timestamp.from(Instant.now());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("pid", "1");
        request.put("endTime", timestamp);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

//        String url = String.format("http://localhost:8081/cron-servers/%s/cron-jobs/%s/procss/%s",
//            cronJob.getServer(), cronJob.getId().toString(), "1");
        String url = String.format("http://10.7.27.11:80/cron-servers/%s/cron-jobs/%s/procss/%s",
            cronJob.getServer(), cronJob.getId().toString(), "1");

        Mockito.when(
            restTemplate.exchange(
                url, HttpMethod.PATCH, entity, String.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // when
        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setNoticeType(NoticeType.End);
        parsedLogDTO.setContent("test");
        parsedLogDTO.setCronJobId(uuid);
        parsedLogDTO.setTimestamp(timestamp);
        parsedLogDTO.setPid("1");

        String cronProcess = cronProcessBusiness.postCronProcess(parsedLogDTO);

        // then
        Assertions.assertEquals(cronProcess, "404");
    }
}