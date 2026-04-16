package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.dto.*;

import java.util.UUID;

public interface UserService {

    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    UserResponseDTO getUserProfile(String email);

    UserResponseDTO updateUserProfile(String email, UpdateProfileRequestDTO updateProfileRequestDTO);

    UserResponseDTO changeUserRole(String adminEmail, UUID targetId, ChangeRoleRequestDTO changeRoleRequestDTO);
}
