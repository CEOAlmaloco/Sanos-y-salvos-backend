package com.javadiseno.sanosysalvos.integration.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalCreateReportDTO {
    private String tipo;
    private String titulo;
    private String descripcion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String ubicacionTexto;
    private Instant fechaReporte;
    private UUID petId;
    private UUID reporterUserId;
}
