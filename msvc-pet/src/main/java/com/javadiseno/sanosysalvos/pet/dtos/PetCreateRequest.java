package com.javadiseno.sanosysalvos.pet.dtos;

import com.javadiseno.sanosysalvos.pet.models.PetSize;
import com.javadiseno.sanosysalvos.pet.models.PetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class PetCreateRequest {

    private UUID ownerUserId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String name;

    @Size(max = 80, message = "La especie no puede superar los 80 caracteres")
    private String species;

    @Size(max = 80, message = "La raza no puede superar los 80 caracteres")
    private String breed;

    @Size(max = 60, message = "El color no puede superar los 60 caracteres")
    private String color;

    private PetSize size;

    private PetStatus status;

    @Size(max = 64, message = "El microchip no puede superar los 64 caracteres")
    private String microchipNumber;

    private UUID primaryPhotoMediaId;
}
