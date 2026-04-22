package com.javadiseno.sanosysalvos.user.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportSummaryResponse {

    private UUID id;
    private UUID petId;
    private UUID reporterUserId;
    private String type;
    private String title;
    private Instant reportedAt;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
