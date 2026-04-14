package com.javadiseno.sanosysalvos.pet.services;

import com.javadiseno.sanosysalvos.pet.client.UserServiceClient;
import com.javadiseno.sanosysalvos.pet.models.PetModel;
import com.javadiseno.sanosysalvos.pet.repositories.PetRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public PetModel createPet(PetModel pet) {
        if (pet == null) {
            throw new IllegalArgumentException("pet must not be null");
        }
        if (pet.getName() == null || pet.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        pet.setId(null);
        pet.setCreatedAt(null);
        pet.setUpdatedAt(null);

        String micro = pet.getMicrochipNumber();
        if (micro != null && !micro.isBlank()) {
            String trimmed = micro.trim();
            if (petRepository.existsByMicrochipNumber(trimmed)) { //trimmed es el numero de microchip sin espacios
                throw new IllegalArgumentException("microchip number already registered");
            }
            pet.setMicrochipNumber(trimmed);
        }

        UUID ownerId = pet.getOwnerUserId();
        if (ownerId != null) {
            try {
                userServiceClient.getUserById(ownerId);
            } catch (FeignException e) {
                if (e.status() == 404) {
                    throw new IllegalArgumentException("owner user does not exist: " + ownerId, e);
                }
                throw e;
            }
        }

        return petRepository.save(pet);
    }
}
