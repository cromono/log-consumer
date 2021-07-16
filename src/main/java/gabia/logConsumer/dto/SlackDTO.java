package gabia.logConsumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlackDTO extends WebhookMessage{
    private String channel;
    private List<Attachment> attachments;

    public void addAttachment (SlackDTO.Attachment attachment) {
        if(this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachment);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Attachment {
        private String title;
        @JsonProperty("title_link")
        private String titleLink;
        private String text;
        private String fallback;
        private String color;
        private String pretext;
        @JsonProperty("author_name")
        private String authorName;
        @JsonProperty("author_link")
        private String authorLink;
        @JsonProperty("author_icon")
        private String authorIcon;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("thumb_url")
        private String thumbUrl;
        private String footer;
        @JsonProperty("footer_icon")
        private String footerIcon;
        private Long ts;
        private List<Field> fields;


    }
}
