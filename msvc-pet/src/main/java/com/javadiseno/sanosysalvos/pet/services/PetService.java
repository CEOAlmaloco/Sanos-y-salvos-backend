package com.javadiseno.sanosysalvos.pet.services;

import com.javadiseno.sanosysalvos.pet.models.PetModel;

import java.util.UUID;

public interface PetService {

    PetModel createPet(PetModel pet);
 
    PetModel updatePet(UUID id, PetModel patch);
}
