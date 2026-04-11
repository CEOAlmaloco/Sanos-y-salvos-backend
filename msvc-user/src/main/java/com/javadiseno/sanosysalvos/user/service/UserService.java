package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.dto.RegisterRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);
}
