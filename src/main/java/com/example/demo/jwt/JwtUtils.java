package com.example.demo.jwt;

import com.example.demo.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private final long EXPIRE_DURATION = 60*60*1000;
    private final String SECRET_KEY = "yKqgqlfzFM6U7e/oXKJvKgwZyEmdjOvp2+9uD9IkOgC4iK1qBva2on/6QyU0FGm1";

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s", user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, this.SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired");
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace");
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            log.error("Signature validation failed");
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
