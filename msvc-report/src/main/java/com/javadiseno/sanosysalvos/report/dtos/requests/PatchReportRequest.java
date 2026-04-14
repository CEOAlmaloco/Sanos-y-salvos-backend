package com.javadiseno.sanosysalvos.report.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

/** Actualización parcial de metadatos (wiki: PATCH /reports/{id}). */
@Data
public class PatchReportRequest {

    private String titulo;

    private String descripcion;

    private BigDecimal latitud;

    private BigDecimal longitud;

    @JsonProperty("ubicacionTexto")
    private String ubicacionTexto;

    @JsonProperty("fechaReporte")
    private Instant fechaReporte;
}
