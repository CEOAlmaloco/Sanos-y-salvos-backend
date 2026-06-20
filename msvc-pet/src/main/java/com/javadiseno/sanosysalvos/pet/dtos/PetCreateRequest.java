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

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 80)
    private String species;

    @Size(max = 80)
    private String breed;

    @Size(max = 60)
    private String color;

    private PetSize size;

    private PetStatus status;

    @Size(max = 64)
    private String microchipNumber;

    private UUID primaryPhotoMediaId;
}
