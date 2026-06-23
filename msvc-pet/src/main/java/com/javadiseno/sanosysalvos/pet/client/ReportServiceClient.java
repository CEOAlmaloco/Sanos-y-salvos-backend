package com.javadiseno.sanosysalvos.pet.client;

import com.javadiseno.sanosysalvos.pet.client.dtos.ReportLiteResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Consulta reportes por mascota (p. ej. antes de borrar). Base: {@code sanos.report-service.url}.
 */
@FeignClient(
        name = "report-service",
        url = "${sanos.report-service.url:http://localhost:8082}",
        path = "")
public interface ReportServiceClient {

    @GetMapping("/api/v1/pets/{petId}/reports/all")
    List<ReportLiteResponse> listReportsByPet(@PathVariable("petId") UUID petId);
}
