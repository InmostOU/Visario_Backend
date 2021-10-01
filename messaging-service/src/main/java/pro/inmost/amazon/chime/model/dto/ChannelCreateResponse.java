package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ChannelCreateResponse {
    private long timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String channelArn;
    private String path;
}
