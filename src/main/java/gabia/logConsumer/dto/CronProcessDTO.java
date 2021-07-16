package gabia.logConsumer.dto;

import java.sql.Timestamp;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CronProcessDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotEmpty @NotBlank
        String pid;
        Timestamp startTime;
        Timestamp endTime;
    }
}
