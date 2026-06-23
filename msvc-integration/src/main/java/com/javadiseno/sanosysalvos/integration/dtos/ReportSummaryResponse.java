package com.javadiseno.sanosysalvos.integration.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportSummaryResponse {
    private UUID id;
    private String type;
    private String title;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Instant reportedAt;
    private UUID petId;
    private UUID reporterUserId;
}
