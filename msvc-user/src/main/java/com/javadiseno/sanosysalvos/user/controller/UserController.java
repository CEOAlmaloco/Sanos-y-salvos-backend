package com.javadiseno.sanosysalvos.user.controller;

import com.javadiseno.sanosysalvos.user.dto.RegisterRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;
import com.javadiseno.sanosysalvos.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        UserResponseDTO response = userService.register(registerRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    };
}
