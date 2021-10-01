package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageEditRequest {
    private String messageId;
    private String content;
    private String channelArn;
}
