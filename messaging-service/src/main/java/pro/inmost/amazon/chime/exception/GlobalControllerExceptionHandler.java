package pro.inmost.amazon.chime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.chime.model.ChimeException;
import software.amazon.awssdk.services.chime.model.ForbiddenException;

import java.util.Date;

@ControllerAdvice
public class GlobalControllerExceptionHandler {


    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<ExceptionModel> handleUserAlreadyAddException(ChannelNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(404)
                        .error(HttpStatus.NOT_FOUND)
                        .message(extractMessage(ex.getMessage()))
                        .path(extractPath(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(ModsCannotLeaveChannelException.class)
    public ResponseEntity<ExceptionModel> handleAdminCannotLeaveChannelException(ModsCannotLeaveChannelException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(403)
                        .error(HttpStatus.FORBIDDEN)
                        .message(extractMessage(ex.getMessage()))
                        .path(extractPath(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionModel> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(403)
                        .error(HttpStatus.FORBIDDEN)
                        .message(ex.getMessage())
                        .path(extractPath(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ExceptionModel> handleMessageNotFoundException(MessageNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(404)
                        .error(HttpStatus.NOT_FOUND)
                        .message(ex.getMessage())
                        .path(extractPath(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(ChimeException.class)
    public ResponseEntity<ExceptionModel> handleChimeException(ChimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ExceptionModel.builder()
                        .timestamp(new Date().getTime())
                        .status(400)
                        .error(HttpStatus.BAD_REQUEST)
                        .message(ex.getMessage())
                        .path(extractPath(ex.getMessage()))
                        .build()
        );
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

