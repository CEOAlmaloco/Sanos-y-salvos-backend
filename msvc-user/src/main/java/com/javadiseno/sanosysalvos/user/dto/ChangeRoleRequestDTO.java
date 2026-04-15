package com.javadiseno.sanosysalvos.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRoleRequestDTO {

    @NotNull(message = "El rol es obligatorio")
    private String role;
}
