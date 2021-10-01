package pro.inmost.amazon.chime.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ContactAddResponse {
    private long timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;
}
