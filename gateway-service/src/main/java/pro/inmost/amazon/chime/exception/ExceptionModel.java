package pro.inmost.amazon.chime.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@Data
public class ExceptionModel {

    private Date timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;

}
