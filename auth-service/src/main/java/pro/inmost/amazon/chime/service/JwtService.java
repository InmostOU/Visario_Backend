package pro.inmost.amazon.chime.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.dto.UserDTO;
import java.util.Date;
import java.util.Map;

public interface JwtService {

    Claims getAllClaimsFromToken(String token);

    Date getExpirationDateFromToken(String token);

    Boolean isTokenExpired(String token);

    String generateToken(UserDTO user, String type);

    String generateTokenWithClaims(Map<String, Object> claims, String username, String type);

    Boolean validateToken(String token);
}
