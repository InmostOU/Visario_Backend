package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageEditResponse {
    private int status;
    private String message;
}
