package com.javadiseno.sanosysalvos.analytics.dtos;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

// SY-70 Esquema del evento que llega desde EventBridge
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Consumir campos nuevos sin romper el servicio
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusEventDTO {

    private String source;

    @JsonProperty("detail-type")
    private String detailType;

    private String detail;

    public static class EventDetailDTO{

        private UUID reportId;

        private UUID petId;

        private UUID userId;

        private Double latitude;

        private Double longitude;

        private Instant eventDate;

        private Double matchScore;

        private UUID matchedReportId;
    }
}
