package com.javadiseno.sanosysalvos.user.exception;

/**
 * SY-2 Se despliega cuando las contraseñas ingresadas no coinciden.
 * El handler global la convierte en 400 Bad Request.
 *
 * @author Christián Mesa
 */
public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        // Se usa `super()` para guardar el mensaje personalizado en la clase padre `RuntimeException`
        super("Las contraseñas no coinciden");
    }
}
