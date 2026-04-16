package com.javadiseno.sanosysalvos.pet.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Vista mínima del usuario expuesta por User Service (JSON compatible con {@code UserResponseDTO}).
 * Usada por Feign para validar {@code ownerUserId} sin acoplar módulos Maven.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSummaryResponse {

    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
}
