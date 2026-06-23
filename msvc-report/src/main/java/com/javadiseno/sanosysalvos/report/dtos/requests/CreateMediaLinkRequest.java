package com.javadiseno.sanosysalvos.report.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMediaLinkRequest {

    @NotBlank(message = "La URL es obligatoria")
    @Size(max = 2048, message = "La URL no puede superar los 2048 caracteres")
    private String url;

    private Integer sortOrder;
}
