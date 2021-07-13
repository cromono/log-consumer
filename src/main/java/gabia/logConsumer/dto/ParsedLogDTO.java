package gabia.logConsumer.dto;

import gabia.logConsumer.entity.Enum.NoticeType;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedLogDTO {

    Timestamp timestamp;
    UUID cronJobId;
    String pid;
    NoticeType noticeType;
    String content;

    public ParsedLogDTO fromMessage(String message) {

        ParsedLogDTO parsedLogDTO = new ParsedLogDTO();
        //Timestamp 추출
        Pattern p = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})");
        Matcher m = p.matcher(message);
        if (m.find()) {
            parsedLogDTO.setTimestamp(Timestamp.valueOf(m.group()));
        }

        //Cron Job ID 추출
        p = Pattern.compile(
            "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");
        m = p.matcher(message);

        if (m.find()) {
            parsedLogDTO.setCronJobId(UUID.fromString(m.group()));
        }

        String[] parsedMessage = message.split(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} )|([0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12} )|(: )");
        String[] excludeContent = parsedMessage[2].split(" ");

        // pid, Message Type 추출 (추가적인 파라미터)
        parsedLogDTO.setPid(excludeContent[1]);
        parsedLogDTO.setNoticeType(NoticeType.valueOf(excludeContent[1]));
        //Content 추출
        parsedLogDTO.setContent(parsedMessage[3]);

        return parsedLogDTO;
    }
}
