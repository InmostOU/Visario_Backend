package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChannelRequest {
    private String mode;
    private String name;
    private String privacy;
    private String metadata;
    private String description;
}
