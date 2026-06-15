package com.javadiseno.sanosysalvos.analytics.dtos;

import lombok.*;

// SY-73 Estadísticas generales del sistema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsResponseDTO {

    private long totalReports;
    private long totalPetsFound;
    private long totalMatches;
}
