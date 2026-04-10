package com.javadiseno.sanosysalvos.user.controller;

import com.javadiseno.sanosysalvos.user.dto.RegisterRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;
import com.javadiseno.sanosysalvos.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * SY-2 - Endpoints de autenticación y registro
 *
 * Ruta: `api/v1/auth`
 * @author Christián Mesa
 */
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * POST /api/v1/auth/register
     *
     * Registra un nuevo usuario en la plataforma.
     * El rol que se le asignara por defecto sera OWNER.
     *
     * @param registerRequestDTO body con los datos correspondientes de registro.
     * @return 201 Created y el UserResponseDTO (sin hashPassword).
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        UserResponseDTO response = userService.register(registerRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    };
}
