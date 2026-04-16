package com.javadiseno.sanosysalvos.user.controller;

import com.javadiseno.sanosysalvos.user.dto.ChangeRoleRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UpdateProfileRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;
import com.javadiseno.sanosysalvos.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * SY-6  Endpoints de User
 */
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // SY-6
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails){

        UserResponseDTO profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity
                .ok(profile);
    }

    //SY-7
    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequestDTO updateProfileRequestDTO
            ){

        UserResponseDTO profile = userService.updateUserProfile(userDetails.getUsername(), updateProfileRequestDTO);
        return ResponseEntity
                .ok(profile);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id,
            @Valid @RequestBody ChangeRoleRequestDTO changeRoleRequestDTO
    ){
        UserResponseDTO profile = userService.changeUserRole(userDetails.getUsername(), id, changeRoleRequestDTO);
        return ResponseEntity
                .ok(profile);
    }
}
