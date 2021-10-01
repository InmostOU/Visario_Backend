package pro.inmost.amazon.chime.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@Builder
public class ExceptionModel {

    private long timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;

}
