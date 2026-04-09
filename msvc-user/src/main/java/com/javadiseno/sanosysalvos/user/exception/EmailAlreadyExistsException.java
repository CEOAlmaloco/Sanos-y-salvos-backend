package com.javadiseno.sanosysalvos.user.exception;

/**
 * SY-2 Se despliega cuando se intenta registrar un email que ya existe.
 * El handler global la convierte en 409 conflict.
 *
 * @author Christián Mesa
 */
public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String email){

        // Se usa `super()` para guardar el mensaje personalizado en la clase padre `RuntimeException`
        super("El email ya está registrado: " + email);
    }
}
