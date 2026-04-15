package com.javadiseno.sanosysalvos.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilleRequestDTO {

    private String profileName;
    private String profilePhone;
}
