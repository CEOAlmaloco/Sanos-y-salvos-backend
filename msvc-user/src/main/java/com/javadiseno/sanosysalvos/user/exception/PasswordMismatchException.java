package com.javadiseno.sanosysalvos.user.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Las contraseñas no coinciden");
    }
}
