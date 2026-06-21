package com.javadiseno.sanosysalvos.report.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Data;

@Data
public class CreateSightingRequest {

    @NotNull(message = "La fecha del avistamiento es obligatoria")
    private Instant spottedAt;

    @Size(max = 2000, message = "Las notas no pueden superar los 2000 caracteres")
    private String notes;
}
