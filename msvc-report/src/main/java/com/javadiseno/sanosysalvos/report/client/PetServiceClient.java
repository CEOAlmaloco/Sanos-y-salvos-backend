package com.javadiseno.sanosysalvos.report.client;

import com.javadiseno.sanosysalvos.report.client.dto.PetSummaryResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Valida existencia de mascota al crear reportes. Base: {@code sanos.pet-service.url}.
 */
@FeignClient(
        name = "pet-service",
        url = "${sanos.pet-service.url:http://localhost:8083}",
        path = "")
public interface PetServiceClient {

    @GetMapping("/api/v1/pets/{petId}")
    PetSummaryResponse getPetById(@PathVariable("petId") UUID petId);
}
