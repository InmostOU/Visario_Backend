package pro.inmost.amazon.chime.exeption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

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
