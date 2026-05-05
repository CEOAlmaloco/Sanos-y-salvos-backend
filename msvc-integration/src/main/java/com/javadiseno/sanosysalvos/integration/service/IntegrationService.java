package com.javadiseno.sanosysalvos.integration.service;

import com.javadiseno.sanosysalvos.integration.dto.ExternalReportRequestDTO;
import com.javadiseno.sanosysalvos.integration.dto.ExternalReportResponseDTO;
import com.javadiseno.sanosysalvos.integration.model.InstitutionApiKey;

public interface IntegrationService {

    ExternalReportResponseDTO processExternalReport(
            InstitutionApiKey institution,
            ExternalReportRequestDTO externalReportRequestDTO
    );
}
