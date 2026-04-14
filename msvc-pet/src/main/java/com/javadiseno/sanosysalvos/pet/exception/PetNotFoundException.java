package com.javadiseno.sanosysalvos.pet.exception;

import java.util.UUID;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(UUID id) {
        super("Mascota no encontrada: " + id);
    }
}
