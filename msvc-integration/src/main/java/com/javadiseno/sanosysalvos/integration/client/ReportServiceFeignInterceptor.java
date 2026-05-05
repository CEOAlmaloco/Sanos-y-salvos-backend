package com.javadiseno.sanosysalvos.integration.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class ReportServiceFeignInterceptor implements RequestInterceptor {
    private static final ThreadLocal<UUID> CURRENT_USER_ID = new ThreadLocal<>();

    private final SecretKey secretKey;
    private final Long expirationMs;

    public ReportServiceFeignInterceptor(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-ms}") Long expirationMs
    ){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public static void setCurrentUserId(UUID userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static void clearCurrentUserId() {
        CURRENT_USER_ID.remove();
    }

    @Override
    public void apply(RequestTemplate template) {
        UUID userId = CURRENT_USER_ID.get();

        if (userId == null) {
            log.warn("ReportServiceFeignInterceptor: no hay userId en ThreadLocal");
            return;
        }

        String token = generateToken(userId);
        template.header("Authorization", "Bearer " + token);
    }

    private String generateToken(UUID userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("role", "INSTITUTION")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }
}
