package com.javadiseno.sanosysalvos.integration.client;

import com.javadiseno.sanosysalvos.integration.dtos.InternalCreateReportDTO;
import com.javadiseno.sanosysalvos.integration.dtos.ReportSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "report-service",
        url = "${sanos.report-service.url:http://localhost:8082}"
)
public interface ReportServiceClient {

    @PostMapping("/api/v1/reports")
    ReportSummaryResponse createReport(@RequestBody InternalCreateReportDTO body);
}
