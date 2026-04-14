package com.javadiseno.sanosysalvos.pet.services;

import com.javadiseno.sanosysalvos.pet.client.UserServiceClient;
import com.javadiseno.sanosysalvos.pet.exception.PetNotFoundException;
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

    @Override
    @Transactional(readOnly = true)
    public PetModel getById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }

    @Override
    @Transactional
    public PetModel updatePet(UUID id, PetModel patch) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (patch == null) {
            throw new IllegalArgumentException("body must not be null");
        }

        PetModel existing = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        applyPatch(existing, patch, id);
        return petRepository.save(existing);
    }

    private void applyPatch(PetModel existing, PetModel patch, UUID id) {
        if (patch.getName() != null) {
            if (patch.getName().isBlank()) {
                throw new IllegalArgumentException("name cannot be blank");
            }
            existing.setName(patch.getName().trim());
        }
        if (patch.getSpecies() != null) {
            existing.setSpecies(patch.getSpecies());
        }
        if (patch.getBreed() != null) {
            existing.setBreed(patch.getBreed());
        }
        if (patch.getColor() != null) {
            existing.setColor(patch.getColor());
        }
        if (patch.getSize() != null) {
            existing.setSize(patch.getSize());
        }
        if (patch.getStatus() != null) {
            existing.setStatus(patch.getStatus());
        }

        if (patch.getMicrochipNumber() != null) {
            String raw = patch.getMicrochipNumber().trim(); //raw es el numero de microchip sin espacios
            if (raw.isEmpty()) {
                existing.setMicrochipNumber(null);
            } else if (!raw.equals(existing.getMicrochipNumber())) {
                if (petRepository.existsByMicrochipNumberAndIdNot(raw, id)) {
                    throw new IllegalArgumentException("microchip number already registered");
                }
                existing.setMicrochipNumber(raw);
            }
        }

        if (patch.getOwnerUserId() != null) {
            UUID newOwner = patch.getOwnerUserId();
            if (existing.getOwnerUserId() == null || !newOwner.equals(existing.getOwnerUserId())) {
                try {
                    userServiceClient.getUserById(newOwner);
                } catch (FeignException e) {
                    if (e.status() == 404) {
                        throw new IllegalArgumentException("owner user does not exist: " + newOwner, e);
                    }
                    throw e;
                }
            }
            existing.setOwnerUserId(newOwner);
        }

        if (patch.getPrimaryPhotoMediaId() != null) {
            existing.setPrimaryPhotoMediaId(patch.getPrimaryPhotoMediaId());
        }
    }
}
