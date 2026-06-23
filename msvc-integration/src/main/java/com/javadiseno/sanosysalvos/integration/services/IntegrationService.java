package com.javadiseno.sanosysalvos.integration.services;

import com.javadiseno.sanosysalvos.integration.dtos.ExternalReportRequestDTO;
import com.javadiseno.sanosysalvos.integration.dtos.ExternalReportResponseDTO;
import com.javadiseno.sanosysalvos.integration.models.InstitutionApiKey;

public interface IntegrationService {

    ExternalReportResponseDTO processExternalReport(
            InstitutionApiKey institution,
            ExternalReportRequestDTO externalReportRequestDTO
    );
}
