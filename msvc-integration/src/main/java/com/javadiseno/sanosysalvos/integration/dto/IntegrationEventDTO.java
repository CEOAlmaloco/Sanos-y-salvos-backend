package com.javadiseno.sanosysalvos.integration.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntegrationEventDTO {

    private String source;
    private String detailType;
    private Detail detail;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private UUID reportId;
        private UUID petId;
        private UUID userId;
        private Double latitude;
        private Double longitude;
        private Instant eventDate;
    }
}
