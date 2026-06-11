package com.javadiseno.sanosysalvos.analytics.dtos;

import lombok.*;

// SY-72 DTO que representa una zona caliente en el mapa de calor.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotZoneDTO {

    // Geohash que identifica la zona (precisión 5)
    private String geohash;

    private Double latitude;

    private Double longitude;

    private long totalEvents;

    private long petReportedCount;
    private long petFoundCount;
    private long matchFoundCount;
    private long petRecoveredCount;


}
