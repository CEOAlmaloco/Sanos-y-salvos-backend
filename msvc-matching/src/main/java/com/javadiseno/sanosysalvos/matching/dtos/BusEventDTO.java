package com.javadiseno.sanosysalvos.matching.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusEventDTO {

    private String source;

    @JsonProperty("detail-type")
    private String detailType;

    private EventDetailDTO detail;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventDetailDTO {

        private UUID reportId;
        private UUID petId;
        private UUID userId;
        private Double latitude;
        private Double longitude;
        private Instant eventDate;
        private String imageObjectKey;
        private UUID matchedReportId;
        private Double matchScore;
    }
}
