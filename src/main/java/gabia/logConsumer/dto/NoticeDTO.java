package gabia.logConsumer.dto;

import gabia.logConsumer.entity.Enum.NoticeType;
import gabia.logConsumer.util.ValidUUID;
import java.sql.Timestamp;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NoticeDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @ValidUUID
        UUID cronJobId;

        NoticeType noticeType;

        @NotEmpty @NotBlank
        String noticeMessage;

        Timestamp noticeCreateDateTime;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        Long notId;
        UUID cronJobId;
        NoticeType noticeType;
        String noticeMessage;
        Timestamp noticeCreateDateTime;
        Boolean isRead;

    }
}
