package com.javadiseno.sanosysalvos.integration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalReportResponseDTO {
    private String externalReportId;
    private UUID internalReportId;
    private String message;
}
