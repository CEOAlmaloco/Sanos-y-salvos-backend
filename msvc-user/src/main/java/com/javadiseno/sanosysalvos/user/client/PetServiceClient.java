package com.javadiseno.sanosysalvos.user.client;

import com.javadiseno.sanosysalvos.user.client.dtos.PetSummaryResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Lista mascotas por dueño 
 */
@FeignClient(
        name = "pet-service-user",
        url = "${sanos.pet-service.url:http://localhost:8083}",
        path = "")
public interface PetServiceClient {

    @GetMapping("/api/v1/pets")
    List<PetSummaryResponse> listPetsForOwner(
            @RequestParam("ownerUserId") UUID ownerUserId,
            @RequestHeader("X-User-Id") UUID xUserId);
}
