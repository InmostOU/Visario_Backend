package pro.inmost.amazon.chime.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;
    private String accessToken;
    private String refreshToken;
    private UserProfile userProfile;

}
