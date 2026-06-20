package com.javadiseno.sanosysalvos.report.client;

import com.javadiseno.sanosysalvos.report.client.dtos.UserSummaryResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Valida reportante por feign API interna de User Service. Base: {@code sanos.user-service.url}.
 */
@FeignClient(
        name = "user-service-report",
        url = "${sanos.user-service.url:http://localhost:8081}",
        path = "/api/v1/internal")
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserSummaryResponse getUserById(@PathVariable("userId") UUID userId);
}
