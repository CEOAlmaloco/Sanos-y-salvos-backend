package com.javadiseno.sanosysalvos.pet.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import lombok.Data;

/** Subconjunto del reporte; basta para comprobar si existen filas asociadas a la mascota. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportLiteResponse {

    private UUID id;
}
