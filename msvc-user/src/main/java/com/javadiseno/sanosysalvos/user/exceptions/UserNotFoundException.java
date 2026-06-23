package com.javadiseno.sanosysalvos.user.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {

        super("Usuario no encontrado: " + email);
    }
}
