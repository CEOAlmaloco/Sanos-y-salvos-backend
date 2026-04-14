package com.javadiseno.sanosysalvos.report.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 * Contrato cercano a la wiki ClickUp (APIS DETALLADAS). {@code pet} sin orquestación Pet Service aún.
 */
@Data
public class CreateReportRequest {

    /** PERDIDO | ENCONTRADO | AVISTAMIENTO o LOST | FOUND | SIGHTING */
    private String tipo;

    private String titulo;

    private String descripcion;

    private BigDecimal latitud;

    private BigDecimal longitud;

    @JsonProperty("ubicacionTexto")
    private String ubicacionTexto;

    @JsonProperty("fechaReporte")
    private Instant fechaReporte;

    @JsonProperty("petId")
    private UUID petId;

    /** Si viene sin petId, la wiki prevé alta de mascota; aquí no está orquestado. */
    private PetMinimoDto pet;

    @JsonProperty("reporterUserId")
    private UUID reporterUserId;

    @JsonProperty("mediaIds")
    private List<UUID> mediaIds;

    @Data
    public static class PetMinimoDto {
        private String nombre;
        private String especie;
        private String raza;
        private String color;
        private String tamano;
    }
}
