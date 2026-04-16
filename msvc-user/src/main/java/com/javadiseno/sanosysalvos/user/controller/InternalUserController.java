package com.javadiseno.sanosysalvos.user.controller;

import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;
import com.javadiseno.sanosysalvos.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * API interna para otros microservicios (Pet, Report). En producción, proteger (mTLS, API key en gateway).
 */
@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
