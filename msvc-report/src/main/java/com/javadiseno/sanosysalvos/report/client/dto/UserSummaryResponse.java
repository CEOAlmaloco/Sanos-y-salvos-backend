package com.javadiseno.sanosysalvos.report.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/** Vista minima de usuario */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSummaryResponse {

    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
}
