package com.javadiseno.sanosysalvos.user.exceptions;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Las contraseñas no coinciden");
    }
}
