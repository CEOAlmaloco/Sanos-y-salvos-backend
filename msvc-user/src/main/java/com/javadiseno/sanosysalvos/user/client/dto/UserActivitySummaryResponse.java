package com.javadiseno.sanosysalvos.user.client.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivitySummaryResponse {

    private UUID userId;
    private int petCount;
    private int reportCount;
}
