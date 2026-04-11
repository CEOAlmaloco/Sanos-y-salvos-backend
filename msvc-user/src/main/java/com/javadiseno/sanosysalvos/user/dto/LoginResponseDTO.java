package com.javadiseno.sanosysalvos.user.dto;

import com.javadiseno.sanosysalvos.user.model.Role;
import lombok.*;

import java.util.UUID;

/**
 * SY-3 DTO de respuesta de login.
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
    private String email;
    private Role role;
}
