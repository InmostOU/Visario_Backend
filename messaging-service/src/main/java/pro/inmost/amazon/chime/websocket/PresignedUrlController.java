package pro.inmost.amazon.chime.websocket;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.amazon.chime.model.dto.BaseResponse;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.repository.AppInstanceUserRepository;
import pro.inmost.amazon.chime.service.UserService;
import java.util.*;

@RestController
@RequestMapping("/websocket/")
public class PresignedUrlController {

    @Value("${access.key.id}")
    private String accessKeyId;

    @Value("${secret.access.key}")
    private String secretAccessKey;

    @Autowired
    private UserService userService;

    @Autowired
    private AppInstanceUserRepository appInstanceUserRepository;

    @SneakyThrows
    @GetMapping("/getPresignedUrl")
    public ResponseEntity<BaseResponse> getPresignedUrl() {
        Map<String, String> credentials = new HashMap<>();
        Map<String, List<String>> queryParams = new HashMap<>();
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());

        credentials.put("accessKeyId", accessKeyId);
        credentials.put("secretAccessKey", secretAccessKey);

        String endpoint = "node001.ue1.ws-messaging.chime.aws";

        queryParams.put("userArn", new ArrayList<String>(Arrays.asList(currentUser.getAppInstanceUserArn())));
        queryParams.put("sessionId", new ArrayList<String>(Arrays.asList(UUID.randomUUID().toString())));

        DefaultSig4 sig4 = new DefaultSig4(credentials);

        String signedUrl = sig4.sigUrl(
                "GET",
                "wss",
                "chime",
                endpoint,
                "/connect",
                "",
                queryParams,
                "us-east-1"

        );

        return ResponseEntity.ok(
                BaseResponse.builder()
                .status(200)
                .message(signedUrl)
                .path("/websocket/getPresignedUrl")
                .build()
        );
    }
}
