package com.javadiseno.sanosysalvos.pet.exceptions;

import java.util.UUID;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(UUID id) {
        super("Mascota no encontrada: " + id);
    }
}
