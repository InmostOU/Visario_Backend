package pro.inmost.amazon.chime.service;


import io.jsonwebtoken.Claims;

import java.util.Date;

public interface JwtService {

    Claims getAllClaimsFromToken(String token);

    Date getExpirationDateFromToken(String token);

    Boolean isTokenExpired(String token);

    Boolean validateToken(String token);

    String getUserFromToken(String token);
}
