package com.example.demo_api_rest.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class JwtUtils {
    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "20c539dd0bf52e21aa53bba3a1e04ea7";
    public static final long EXPIRE_DAYS = 0;

    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 10;


    private JwtUtils() {

    }

    private static javax.crypto.SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date expireDate (Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());

    }

    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date();
        Date limit = expireDate(issuedAt);
        String token = Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(limit)
                .signWith(generateKey())
                .claim("role", role)
                .compact();
        return new JwtToken(token);
    }
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token)).getPayload();
        } catch (JwtException ex) {
            log.error("Token invalido {}", ex.getMessage());
        }
        return null;
    }

    public static String getUserNameFromToken (String token) {
        return Objects.requireNonNull(getClaimsFromToken(token)).getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error("Token invalido {}", ex.getMessage());
        }
        return false;
    }

    private static String refactorToken (String token) {
        if(token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
        }

        return token;
    }
}
