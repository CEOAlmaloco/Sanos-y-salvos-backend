package com.javadiseno.sanosysalvos.pet.dtos;

import com.javadiseno.sanosysalvos.pet.models.PetSize;
import com.javadiseno.sanosysalvos.pet.models.PetStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/** Respuesta JSON para otros microservicios y clientes .no expone detalles JPA */
@Data
public class PetResponse {

    private UUID id;
    private UUID ownerUserId;
    private String name;
    private String species;
    private String breed;
    private String color;
    private PetSize size;
    private PetStatus status;
    private String microchipNumber;
    private UUID primaryPhotoMediaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
