package com.javadiseno.sanosysalvos.integration.controller;

import com.javadiseno.sanosysalvos.integration.dto.ExternalReportRequestDTO;
import com.javadiseno.sanosysalvos.integration.dto.ExternalReportResponseDTO;
import com.javadiseno.sanosysalvos.integration.model.InstitutionApiKey;
import com.javadiseno.sanosysalvos.integration.security.ApiKeyAuthenticationFilter;
import com.javadiseno.sanosysalvos.integration.service.IntegrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/integration")
@RequiredArgsConstructor
public class IntegrationController {
    private final IntegrationService integrationService;

        @PostMapping("/reports")
    public ResponseEntity<ExternalReportResponseDTO> receiveReport(
            @Valid @RequestBody ExternalReportRequestDTO externalReportRequestDTO,
            HttpServletRequest request
    ){
        InstitutionApiKey institution = (InstitutionApiKey)
                request.getAttribute(ApiKeyAuthenticationFilter.INSTITUTION_ATTRIBUTE);

        ExternalReportResponseDTO response = integrationService
                .processExternalReport(institution, externalReportRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
