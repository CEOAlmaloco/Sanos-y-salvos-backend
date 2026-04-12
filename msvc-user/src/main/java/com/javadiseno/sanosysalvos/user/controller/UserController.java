package com.javadiseno.sanosysalvos.user.controller;

import com.javadiseno.sanosysalvos.user.dto.*;
import com.javadiseno.sanosysalvos.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * SY-2 | SY-3 Endpoints de autenticación
 */
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // SY-2
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        UserResponseDTO response = userService.register(registerRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    };

    // SY-3
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO response = userService.login(loginRequestDTO);
        return ResponseEntity
                .ok(response);
    }
}
