package com.javadiseno.sanosysalvos.report.exceptions;

import com.javadiseno.sanosysalvos.report.dtos.ErrorDTO;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo de errores HTTP para msvc-report .
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errorMap.put(fe.getField(), fe.getDefaultMessage());
        }

        ErrorDTO body = new ErrorDTO();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setMessage("Errores de validación");
        body.setDetails("Se encontraron " + errorMap.size() + " error(es)");
        body.setTimestamp(LocalDateTime.now());
        body.setPath(request.getRequestURI());
        body.setErrors(errorMap);

        log.warn("Validación en {}: {}", request.getRequestURI(), errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDTO body = new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI());
        log.warn("404 en {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDTO> handleFeign(FeignException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        String message = "Error al comunicar con otro microservicio";
        String details = ex.getMessage();

        if (ex.status() == 404) {
            status = HttpStatus.NOT_FOUND;
            message = "Recurso no encontrado en servicio externo";
        } else if (ex.status() >= 400 && ex.status() < 500) {
            status = HttpStatus.resolve(ex.status()) != null ? HttpStatus.valueOf(ex.status()) : status;
            message = "Error en solicitud a servicio externo";
        }

        ErrorDTO body = new ErrorDTO(status.value(), message, details, request.getRequestURI());
        log.error("Feign en {} status={}: {}", request.getRequestURI(), ex.status(), details);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorDTO> handleReport(ReportException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String lower = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        if (lower.contains("no encontrado") || lower.contains("no existe")) {
            status = HttpStatus.NOT_FOUND;
        } else if (lower.contains("ya existe")
                || lower.contains("duplicado")
                || lower.contains("conflicto")
                || lower.contains("ya está cerrado")) {
            status = HttpStatus.CONFLICT;
        } else if (lower.contains("no autorizado") || lower.contains("acceso denegado")) {
            status = HttpStatus.FORBIDDEN;
        }

        ErrorDTO body = new ErrorDTO(
                status.value(),
                "Error en operación de reporte",
                ex.getMessage(),
                request.getRequestURI());

        log.warn("ReportException en {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorDTO body = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud inválida",
                ex.getMessage(),
                request.getRequestURI());
        log.warn("IllegalArgument en {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneric(Exception ex, HttpServletRequest request) {
        String details = "Ocurrió un error inesperado.";
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            details = ex.getClass().getSimpleName() + ": " + (ex.getMessage() != null ? ex.getMessage() : "");
        }
        ErrorDTO body = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                details,
                request.getRequestURI());
        log.error("Error no manejado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
