package com.javadiseno.sanosysalvos.report.exceptions;

/**
 * Recurso solicitado no existe (reporte, avistamiento, medio, etc.).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
