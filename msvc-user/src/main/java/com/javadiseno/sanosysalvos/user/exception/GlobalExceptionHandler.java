package com.javadiseno.sanosysalvos.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * SY-2 Manejo centralizado de excepciones
 *
 * Convierte excepciones en respuestas HTTP con estructura consistente.
 *
 * @author Christián Mesa
 */
@RestControllerAdvice // Centraliza el manejo de errores y devuelve automaticamente las respuestas en formato JSON
public class GlobalExceptionHandler{

    // -- 409 Conflict: Email duplicado ---------------------------

    @ExceptionHandler(EmailAlreadyExistsException.class) // Captura la excepción `EmailAlreadyExistsException`
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
      // Respuesta HTTP final.
      return ResponseEntity
              .status(HttpStatus.CONFLICT) // Estado HTTP `409 Conflict`
              .body(new ErrorResponse(     // Cuerpo del estado HTTP
                      HttpStatus.CONFLICT.value(),
                      ex.getMessage(),
                      LocalDateTime.now()
              ));
    }

    // -- 400 Bad Request: Fallo en la validación @Valid ---------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsResponse> handleValidationErrors (MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>(); // Par `clave/valor` donde la clave es el campo y el valor es el mensaje de error.

        /**
         * Convierte los errores de validación de Spring en formato `clave/valor`:
         *  - Recorre cada error de validación y los guarda en la interfaz `errors` con el campo y el mensaje de error.
         *  - Los usuarios podran entender con mayor facilidad el error.
         */
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorsResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error de validación en los datos enviados",
                        errors,
                        LocalDateTime.now()
                ));
    }

    // -- 500 Internal Server Error: Cualquier excepción no manejada ---------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
      return ResponseEntity
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse(
                      HttpStatus.INTERNAL_SERVER_ERROR.value(),
                      "Error interno del servidor",
                      LocalDateTime.now()
              ));
    }

    // -- Records de Respuesta ------------------

    public record ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp
    ){}

    public record ValidationErrorsResponse(
            int status,
            String message,
            Map<String, String> errors,
            LocalDateTime timestamp
    ){}
}

