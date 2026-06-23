package com.javadiseno.sanosysalvos.report.dtos;

import com.javadiseno.sanosysalvos.report.models.ReportModel.ReportStatus;
import com.javadiseno.sanosysalvos.report.models.ReportModel.ReportType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

/** Respuesta JSON de reporte; no expone entidades JPA ni relaciones internas. */
@Data
public class ReportResponse {

    private UUID id;
    private UUID petId;
    private UUID reporterUserId;
    private ReportType type;
    private String title;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String locationDescription;
    private Instant reportedAt;
    private ReportStatus status;
    private Instant resolvedAt;
    private Instant createdAt;
}
