package com.javadiseno.sanosysalvos.analytics.dtos;

import lombok.*;

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

    private Long totalEvents;

    private Long petReportedCount;
    private Long petFoundCount;
    private Long matchFoundCount;
    private Long petRecoveredCount;


}
