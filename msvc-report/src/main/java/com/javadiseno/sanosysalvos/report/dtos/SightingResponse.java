package com.javadiseno.sanosysalvos.report.dtos;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class SightingResponse {

    private UUID id;
    private UUID reportId;
    private Instant spottedAt;
    private String notes;
    private Instant createdAt;
}
