package com.javadiseno.sanosysalvos.report.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/** Contrato de entrada alineado a la wiki; campos JSON legacy en español vía {@link JsonProperty}. */
@Data
public class CreateReportRequest {

    /** PERDIDO | ENCONTRADO | AVISTAMIENTO o LOST | FOUND | SIGHTING */
    @JsonProperty("tipo")
    @NotBlank(message = "El tipo es obligatorio")
    private String type;

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

    @NotNull(message = "El petId es obligatorio")
    private UUID petId;

    /** Si viene sin petId, la wiki prevé alta de mascota; aquí no está orquestado. */
    @Valid
    private EmbeddedPetRequest pet;

    private UUID reporterUserId;

    private List<UUID> mediaIds;

    @Data
    public static class EmbeddedPetRequest {

        @JsonProperty("nombre")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        private String name;

        @JsonProperty("especie")
        @Size(max = 80, message = "La especie no puede superar los 80 caracteres")
        private String species;

        @JsonProperty("raza")
        @Size(max = 80, message = "La raza no puede superar los 80 caracteres")
        private String breed;

        @Size(max = 60, message = "El color no puede superar los 60 caracteres")
        private String color;

        @JsonProperty("tamano")
        @Size(max = 20, message = "El tamaño no puede superar los 20 caracteres")
        private String size;
    }
}
