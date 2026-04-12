package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * SY-4 Implementación de JwtService.
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final Long expirationMs;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-ms}") Long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    @Override
    public String generateToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    @Override
    public boolean isTokenValid(String token){
        try{
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        }catch (JwtException | IllegalArgumentException e){
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
