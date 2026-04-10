package com.javadiseno.sanosysalvos.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * SY-2 DTO de entrada para el registro de un nuevo usuario
 *
 * Contiene los datos que el cliente debe enviar.
 * El rol asigna OWNER por defecto al registrarse.
 *
 * @author Christián Mesa
 */

@Data // Genera automáticamente el codigo repetitivo común (getters, setters, etc).
@NoArgsConstructor // Genera un constructor sin argumentos para la clase RegisterRequestDTO.
@AllArgsConstructor // Genera un constructor con argumentos para cada campo de la clase RegisterRequestDTO.
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", // Verifica si existe una letra minúscula, mayúscula y un número.
            message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    private String password;

    @NotBlank(message = "Debes confirmar la contraseña")
    private String confirmPassword;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String phone;  // Opcional
}
