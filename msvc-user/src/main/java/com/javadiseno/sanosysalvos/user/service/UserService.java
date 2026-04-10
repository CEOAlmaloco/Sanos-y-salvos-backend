package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.dto.RegisterRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;

/**
 * SY-2 - Definición de los metodos del servicio de usuario
 *
 * @author Christián Mesa
 */
public interface UserService {

    /**
     * Registra un nuevo usuario en la plataforma
     *
     * @param registerRequestDTO datos de registro enviados por el cliente
     * @return Un objeto UserResponseDTO con los datos del usuario creado (sin hashPassword)
     * @throws com.javadiseno.sanosysalvos.user.exception.EmailAlreadyExistsException si el email ya esta registrado
     * @throws com.javadiseno.sanosysalvos.user.exception.PasswordMismatchException si el campo password y confirmPassword no coinciden
     */
    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);
}
