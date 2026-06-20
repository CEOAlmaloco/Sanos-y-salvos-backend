package com.javadiseno.sanosysalvos.user.dtos;

import com.javadiseno.sanosysalvos.user.models.Role;
import lombok.*;

import java.util.UUID;

/**
 * SY-3 | SY-4 DTO de respuesta de login.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String tokenType;
    private UUID userId;
    private String name;
    private String lastName;
    private String email;
    private Role role;
}
