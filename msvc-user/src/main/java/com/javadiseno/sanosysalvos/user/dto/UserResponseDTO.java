package com.javadiseno.sanosysalvos.user.dto;

import com.javadiseno.sanosysalvos.user.model.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SY-2 DTO de salida para respuestas relacionadas con el usuario
 *
 *  - No incluye datos sensibles (hashPassword, etc).
 *  - Se usara en distintos procesos:
 *      - Registro (SY-2)
 *      - Por ver...
 *
 * @author Christián Mesa
 */

@Data // Genera automáticamente el codigo repetitivo común (getters, setters, etc).
@NoArgsConstructor // Genera un constructor sin argumentos para la clase UserResponseDTO.
@AllArgsConstructor // Genera un constructor con argumentos para cada campo de la clase UserResponseDTO.
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
}
