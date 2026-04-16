package com.javadiseno.sanosysalvos.pet.client;

import com.javadiseno.sanosysalvos.pet.client.dto.UserSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Llamadas a User Service para validar dueños/reportantes al crear o asociar mascotas (SY-11, SY-24).
 * URL base: {@code sanos.user-service.url} (p. ej. Docker o API Gateway).
 */
@FeignClient(
        name = "user-service",
        url = "${sanos.user-service.url:http://localhost:8081}",
        path = "/api/v1/internal"
)
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserSummaryResponse getUserById(@PathVariable("userId") UUID userId);
}
