package com.javadiseno.sanosysalvos.user.exception;

/**
 * SY-3 Excepción de credenciales incorrectas en Login
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Credenciales inválidas");
    }
}
