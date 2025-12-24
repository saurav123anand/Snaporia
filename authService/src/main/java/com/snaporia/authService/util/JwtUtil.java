package com.snaporia.authService.util;

import com.snaporia.authService.config.ExternalConfig;
import com.snaporia.authService.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final ExternalConfig externalConfig;

    public String generateToken(User user) {
        Map<String, Object> authorityClaims = new HashMap<>();
//        SecretKey key =
//                Keys.hmacShaKeyFor(Decoders.BASE64.decode(externalConfig.getJwtSecret()));
        SecretKey key = Keys.hmacShaKeyFor(
                externalConfig.getJwtSecret().getBytes());
        authorityClaims.put(
                "authorities",
                user.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList()
        );
        Instant now = Instant.now();
        return Jwts.builder().
                claims(authorityClaims).
                subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(
                        Date.from(now.plusMillis(
                                externalConfig.getJwtExpiration()
                        ))
                )
                .signWith(key,Jwts.SIG.HS256)
                .compact();

    }
}
