package com.javadiseno.sanosysalvos.report.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Cuerpo opcional para POST .../resolve. */
@Data
public class ResolveReportRequest {

    @JsonProperty("motivo")
    @Size(max = 100, message = "El motivo no puede superar los 100 caracteres")
    private String reason;

    @JsonProperty("nota")
    @Size(max = 2000, message = "La nota no puede superar los 2000 caracteres")
    private String note;
}
