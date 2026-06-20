package com.javadiseno.sanosysalvos.media.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import software.amazon.awssdk.services.s3.model.S3Exception;

@RestControllerAdvice //el advice es el que maneja las excepciones
public class GlobalExceptionHandler {
    
    @ExceptionHandler(S3Exception.class) //el handler es el que maneja la excepcion
    public ResponseEntity<ErrorBody> s3(S3Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorBody("S3_ERROR", e.awsErrorDetails() != null
                        ? e.awsErrorDetails().errorMessage()
                        : e.getMessage()));
    }

    public record ErrorBody(String code, String message) {}
}
