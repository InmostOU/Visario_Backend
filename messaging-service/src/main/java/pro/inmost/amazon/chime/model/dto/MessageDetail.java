package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDetail {
    private Long id;
    private String content;
    private long createdTimestamp;
    private long lastEditedTimestamp;
    private String messageId;
    private String metadata;
    private boolean redacted;
    private String senderArn;
    private String senderName;
    private String type;
    private boolean isFromCurrentUser;
    private String channelArn;
    private Boolean delivered;
}
