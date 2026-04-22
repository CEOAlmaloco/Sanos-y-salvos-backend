package com.javadiseno.sanosysalvos.user.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

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
    private LocalDateTime createdAt;
}
