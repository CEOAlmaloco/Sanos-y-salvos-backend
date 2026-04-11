package com.javadiseno.sanosysalvos.user.dto;

import com.javadiseno.sanosysalvos.user.model.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
}
