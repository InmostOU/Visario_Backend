package pro.inmost.amazon.chime.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.Date;
import java.util.Map;

@Getter
public class ValidationExceptionModel extends ExceptionModel {

    private Map<String, String> errors;

    public ValidationExceptionModel(Date timestamp, int status, HttpStatus error, String message, String path, Map<String, String> errors) {
        super(timestamp, status, error, message, path);
        this.errors = errors;
    }
}
