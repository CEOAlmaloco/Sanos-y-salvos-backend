package com.javadiseno.sanosysalvos.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * SY-3 DTO de entrada para login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
