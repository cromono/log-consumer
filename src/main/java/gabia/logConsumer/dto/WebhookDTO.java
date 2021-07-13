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
public class WebhookDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        String url;
        String text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendText {
        String text;
    }

}
