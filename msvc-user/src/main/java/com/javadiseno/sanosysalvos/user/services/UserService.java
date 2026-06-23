package com.javadiseno.sanosysalvos.user.services;

import com.javadiseno.sanosysalvos.user.dtos.*;

import java.util.UUID;

public interface UserService {

    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    UserResponseDTO getUserProfile(String email);

    UserResponseDTO updateUserProfile(String email, UpdateProfileRequestDTO updateProfileRequestDTO);

    UserResponseDTO changeUserRole(String adminEmail, UUID targetId, ChangeRoleRequestDTO changeRoleRequestDTO);

    /**
     * Expuesto para otros microservicios (Pet, Report) vía {@code /api/v1/internal/users/{id}}.
     */
    UserResponseDTO getUserById(UUID id);
}
