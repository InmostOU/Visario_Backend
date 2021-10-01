package pro.inmost.amazon.chime.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionModel> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionModel.builder()
                .timestamp(new Date().getTime())
                .status(404)
                .error(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .path("/contact/add")
                .build()
        );
    }

    @ExceptionHandler(UserAlreadyAddException.class)
    public ResponseEntity<ExceptionModel> handleUserAlreadyAddException(UserAlreadyAddException ex) {
        return ResponseEntity.status(HttpStatus.IM_USED).body(
                ExceptionModel.builder()
                .timestamp(new Date().getTime())
                .status(226)
                .error(HttpStatus.IM_USED)
                .message(ex.getMessage())
                .path("/contact/add")
                .build()
        );
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ExceptionModel> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.IM_USED).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(226)
                        .error(HttpStatus.IM_USED)
                        .message(ex.getMessage())
                        .path("/user/profile/update")
                        .build()
        );
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ExceptionModel> handleUserAlreadyAddException(ContactNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.IM_USED).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(404)
                        .error(HttpStatus.NOT_FOUND)
                        .message(extractMessage(ex.getMessage()))
                        .path(extractPath(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAnyOtherException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private String extractPath(String message) {
        String[] exception = message.split("");
        String path = "";

        for (int i = 0; i < exception.length; i++) {
              if (exception[i].equals("{"))
                  path = message.substring(i + 1, message.length() - 1);
        }

        return path;
    }

    private String extractMessage(String exceptionMessage) {
        String[] exception = exceptionMessage.split(" ");
        String message = "";

        for (int i = 0; i < exception.length; i++) {
            if (exception[i].equals("Path:"))
                for (int j = 0; j < i; j++) {
                    message += exception[j] + " ";
                }
        }

        return message;
    }
}

