package com.javadiseno.sanosysalvos.integration.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalReportRequestDTO {

    @NotBlank(message = "El ID externo del reporte es obligatorio")
    @Size(max = 100, message = "El ID externo no puede superar los 100 caracteres")
    private String externalReportId;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Pattern(
            regexp = "LOST|FOUND",
            message = "El tipo debe ser LOST o FOUND"
    )
    private String reportType;

    @NotNull(message = "El petId es obligatorio")
    private UUID petId;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50, message = "La especie no puede superar los 50 caracteres")
    private String species;

    @Size(max = 50, message = "La raza no puede superar los 50 caracteres")
    private String breed;

    @Size(max = 50, message = "El color no puede superar los 50 caracteres")
    private String color;

    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    private String title;

    @Size(max = 600, message = "La descripción no puede superar los 600 caracteres")
    private String description;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "Latidud mínima: -90")
    @DecimalMax(value = "90.0", message = "Latidud máxima: 90")
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "Latidud mínima: -180")
    @DecimalMax(value = "180.0", message = "Latidud máxima: 180")
    private Double longitude;

    @Size(max = 2000, message = "La descripción de la locación no puede superar los 2000 caracteres")
    private String locationText;

    private LocalDateTime eventDate;
}
