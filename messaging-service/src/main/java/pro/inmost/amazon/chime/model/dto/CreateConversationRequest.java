package pro.inmost.amazon.chime.model.dto;

import lombok.Data;

@Data
public class CreateConversationRequest {
    private String participantArn;
    private String metadata;
}
