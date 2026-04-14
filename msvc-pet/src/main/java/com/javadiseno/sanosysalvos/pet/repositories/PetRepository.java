package com.javadiseno.sanosysalvos.pet.repositories;

import com.javadiseno.sanosysalvos.pet.models.PetModel;
import com.javadiseno.sanosysalvos.pet.models.PetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetModel, UUID> {

    List<PetModel> findByOwnerUserIdOrderByCreatedAtDesc(UUID ownerUserId);

    Optional<PetModel> findByMicrochipNumber(String microchipNumber);

    boolean existsByMicrochipNumber(String microchipNumber);

    /** Microchip unico comparado con otras mascotas al actualizar o crear una mascota */
    boolean existsByMicrochipNumberAndIdNot(String microchipNumber, UUID id);

    List<PetModel> findByStatus(PetStatus status);
}
