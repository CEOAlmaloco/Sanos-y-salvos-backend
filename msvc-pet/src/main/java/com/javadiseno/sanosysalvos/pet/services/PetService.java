package com.javadiseno.sanosysalvos.pet.services;

import com.javadiseno.sanosysalvos.pet.models.PetModel;

import java.util.List;
import java.util.UUID;

public interface PetService {

    PetModel createPet(PetModel pet);

    PetModel getById(UUID id);

    List<PetModel> listPetsForOwner(UUID ownerUserId, UUID actingUserId);

    PetModel updatePet(UUID id, PetModel patch);
}
