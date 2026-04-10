package com.javadiseno.sanosysalvos.report.dtos;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta JSON para errores del microservicio de reportes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private String path;
    /** errores por campo validacion */
    private Map<String, String> errors;

    public ErrorDTO(int status, String message, String details, String path) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
