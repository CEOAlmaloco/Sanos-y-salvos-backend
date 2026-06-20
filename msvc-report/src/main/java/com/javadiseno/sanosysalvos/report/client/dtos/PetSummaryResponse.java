package com.javadiseno.sanosysalvos.report.client.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/** Vista minima de mascota */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetSummaryResponse {

    private UUID id;
    private UUID ownerUserId;
    private String name;
    private String species;
    private String breed;
    private String color;
    private String size;
    private String status;
    private String microchipNumber;
    private UUID primaryPhotoMediaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
