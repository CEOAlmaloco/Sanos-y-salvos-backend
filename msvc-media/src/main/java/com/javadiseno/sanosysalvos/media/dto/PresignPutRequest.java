package com.javadiseno.sanosysalvos.media.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignPutRequest(
        @NotBlank String fileName,
        String contentType,
        /** carpeta logica opcional cmo reports/123 */
        String prefix
) {}
