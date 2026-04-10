package com.javadiseno.sanosysalvos.report.exceptions;

/**
 * Errores de dominio del Report Service (reglas de negocio, datos inconsistentes).
 * Traducidos a HTTP por {@code GlobalExceptionHandler}.
 */
public class ReportException extends RuntimeException {

    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportException(Throwable cause) {
        super(cause);
    }
}
