package com.javadiseno.sanosysalvos.user.client;

import com.javadiseno.sanosysalvos.user.client.dtos.ReportSummaryResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Reportes cuyo reportante es el usuario indicado. */
@FeignClient(
        name = "report-service-user",
        url = "${sanos.report-service.url:http://localhost:8082}",
        path = "")
public interface ReportServiceClient {

    @GetMapping("/api/v1/users/{userId}/reports")
    List<ReportSummaryResponse> listReportsForUser(@PathVariable("userId") UUID userId);
}
