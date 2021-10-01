package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
public class BaseResponse {
    private long timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;

    public static BaseResponse standard() {
        return BaseResponse.builder()
                .timestamp(new Date().getTime())
                .status(200)
                .message("OK")
                .build();
    }
}
