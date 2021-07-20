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
        String message = "{\n"
            + "  \"@timestamp\": \"2021-07-20T04:21:58.521Z\",\n"
            + "  \"@metadata\": {\n"
            + "    \"beat\": \"filebeat\",\n"
            + "    \"type\": \"_doc\",\n"
            + "    \"version\": \"7.13.3\"\n"
            + "  },\n"
            + "  \"message\": \"2021-07-20 13:21:49.149 550e8400-e29b-41d4-a716-446655440000 21455 End: Test End Log for Webhook Service.\",\n"
            + "  \"input\": {\n"
            + "    \"type\": \"log\"\n"
            + "  },\n"
            + "  \"fields\": {\n"
            + "    \"log_topic\": \"statusLog\"\n"
            + "  },\n"
            + "  \"host\": {\n"
            + "    \"ip\": [\n"
            + "      \"10.7.27.9\",\n"
            + "      \"fe80::d00d:3eff:fe3f:de48\",\n"
            + "      \"172.17.0.1\",\n"
            + "      \"fe80::42:32ff:fe11:2c60\",\n"
            + "      \"172.19.0.1\",\n"
            + "      \"fe80::42:1ff:fe6d:a62a\",\n"
            + "      \"fe80::74f4:fdff:fe19:e19d\",\n"
            + "      \"172.20.0.1\",\n"
            + "      \"fe80::42:1aff:fee7:4610\",\n"
            + "      \"fe80::f0c7:64ff:fe0e:f1e4\",\n"
            + "      \"fe80::8c81:8bff:fe78:d5a8\",\n"
            + "      \"fe80::a0cf:73ff:fe11:3396\",\n"
            + "      \"fe80::b071:1eff:fea2:5a06\",\n"
            + "      \"fe80::243c:45ff:fe91:9bf0\",\n"
            + "      \"172.18.0.1\",\n"
            + "      \"fe80::42:d6ff:fe80:909\",\n"
            + "      \"fe80::244b:2fff:fe1d:79df\",\n"
            + "      \"fe80::c40b:ccff:feb8:b742\",\n"
            + "      \"fe80::707b:76ff:fe59:da36\",\n"
            + "      \"fe80::38e3:78ff:fed3:a0f0\"\n"
            + "    ],\n"
            + "    \"mac\": [\n"
            + "      \"d2:0d:3e:3f:de:48\",\n"
            + "      \"02:42:32:11:2c:60\",\n"
            + "      \"02:42:01:6d:a6:2a\",\n"
            + "      \"76:f4:fd:19:e1:9d\",\n"
            + "      \"02:42:1a:e7:46:10\",\n"
            + "      \"f2:c7:64:0e:f1:e4\",\n"
            + "      \"8e:81:8b:78:d5:a8\",\n"
            + "      \"a2:cf:73:11:33:96\",\n"
            + "      \"b2:71:1e:a2:5a:06\",\n"
            + "      \"26:3c:45:91:9b:f0\",\n"
            + "      \"02:42:d6:80:09:09\",\n"
            + "      \"26:4b:2f:1d:79:df\",\n"
            + "      \"c6:0b:cc:b8:b7:42\",\n"
            + "      \"72:7b:76:59:da:36\",\n"
            + "      \"3a:e3:78:d3:a0:f0\"\n"
            + "    ],\n"
            + "    \"hostname\": \"gcloud-seoul-477ad223c8ae57ea56d3c6fd32491578\",\n"
            + "    \"architecture\": \"x86_64\",\n"
            + "    \"name\": \"gcloud-seoul-477ad223c8ae57ea56d3c6fd32491578\",\n"
            + "    \"os\": {\n"
            + "      \"platform\": \"centos\",\n"
            + "      \"version\": \"7 (Core)\",\n"
            + "      \"family\": \"redhat\",\n"
            + "      \"name\": \"CentOS Linux\",\n"
            + "      \"kernel\": \"3.10.0-1160.15.2.el7.x86_64\",\n"
            + "      \"codename\": \"Core\",\n"
            + "      \"type\": \"linux\"\n"
            + "    },\n"
            + "    \"id\": \"861b9bdf6ddf69d777cae8e4ce6e5b99\",\n"
            + "    \"containerized\": false\n"
            + "  },\n"
            + "  \"agent\": {\n"
            + "    \"name\": \"gcloud-seoul-477ad223c8ae57ea56d3c6fd32491578\",\n"
            + "    \"type\": \"filebeat\",\n"
            + "    \"version\": \"7.13.3\",\n"
            + "    \"hostname\": \"gcloud-seoul-477ad223c8ae57ea56d3c6fd32491578\",\n"
            + "    \"ephemeral_id\": \"5cd8e3f9-842d-4a60-b558-47cedca116ce\",\n"
            + "    \"id\": \"44beb1b1-8f4c-4e7b-b09b-dcf87ab2f472\"\n"
            + "  },\n"
            + "  \"ecs\": {\n"
            + "    \"version\": \"1.8.0\"\n"
            + "  },\n"
            + "  \"container\": {\n"
            + "    \"id\": \"2021-07-20_550e8400-e29b-41d4-a716-446655440000.log\"\n"
            + "  },\n"
            + "  \"log\": {\n"
            + "    \"offset\": 1850,\n"
            + "    \"file\": {\n"
            + "      \"path\": \"/var/log/cronlogs/status/2021-07-20_550e8400-e29b-41d4-a716-446655440000.log\"\n"
            + "    }\n"
            + "  }\n"
            + "}\n";

        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        parsedLogDTO.setPid("21455");
        parsedLogDTO.setTimestamp(Timestamp.valueOf("2021-07-20 13:21:49.149"));
        parsedLogDTO.setCronJobId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        parsedLogDTO.setContent("Test End Log for Webhook Service.");
        parsedLogDTO.setNoticeType(NoticeType.End);

        given(cronLogBusiness.saveLog(parsedLogDTO)).willReturn(parsedLogDTO);
        // When
        ParsedLogDTO result = cronLogService.addCronLog(message);
        // Then
        Assertions.assertThat(parsedLogDTO).isEqualTo(result);
    }
}