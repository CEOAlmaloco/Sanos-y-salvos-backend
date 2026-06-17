package com.javadiseno.sanosysalvos.report.dtos;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEventDTO {

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
