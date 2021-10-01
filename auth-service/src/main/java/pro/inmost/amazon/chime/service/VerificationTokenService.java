package pro.inmost.amazon.chime.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface VerificationTokenService {

    Long createVerification(String email, Long userId);

    ResponseEntity<String> verifyUser(String token);
}
