package com.javadiseno.sanosysalvos.report.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

/** Actualización parcial de metadatos (PATCH /reports/{id}). */
@Data
public class PatchReportRequest {

    @JsonProperty("titulo")
    @Size(max = 500, message = "El título no puede superar los 500 caracteres")
    private String title;

    @JsonProperty("descripcion")
    @Size(max = 5000, message = "La descripción no puede superar los 5000 caracteres")
    private String description;

    @JsonProperty("latitud")
    @DecimalMin(value = "-90.0", message = "La latitud mínima es -90")
    @DecimalMax(value = "90.0", message = "La latitud máxima es 90")
    private BigDecimal latitude;

    @JsonProperty("longitud")
    @DecimalMin(value = "-180.0", message = "La longitud mínima es -180")
    @DecimalMax(value = "180.0", message = "La longitud máxima es 180")
    private BigDecimal longitude;

    @JsonProperty("ubicacionTexto")
    @Size(max = 2000, message = "La ubicación no puede superar los 2000 caracteres")
    private String locationText;

    @JsonProperty("fechaReporte")
    private Instant reportedAt;
}
