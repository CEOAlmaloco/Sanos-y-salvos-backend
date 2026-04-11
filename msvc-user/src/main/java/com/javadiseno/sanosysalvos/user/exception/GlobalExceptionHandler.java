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

@RestControllerAdvice
public class GlobalExceptionHandler{

        @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
      return ResponseEntity
                          .status(HttpStatus.CONFLICT)
                          .body(new ErrorResponse(
                      HttpStatus.CONFLICT.value(),
                      ex.getMessage(),
                      LocalDateTime.now()
              ));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ValidationErrorsResponse> handlePasswordMismatch(PasswordMismatchException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("confirmPassword", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorsResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error de coincidencia",
                        errors,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsResponse> handleValidationErrors (MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();
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

