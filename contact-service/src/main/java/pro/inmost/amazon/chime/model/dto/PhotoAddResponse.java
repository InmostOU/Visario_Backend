package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Setter
@Getter
public class PhotoAddResponse {
    private long timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;
}
