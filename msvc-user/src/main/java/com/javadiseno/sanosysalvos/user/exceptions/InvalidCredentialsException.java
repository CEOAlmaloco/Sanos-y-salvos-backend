package com.javadiseno.sanosysalvos.user.exceptions;

/**
 * SY-3 Excepción de credenciales incorrectas en Login
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Credenciales inválidas");
    }
}
