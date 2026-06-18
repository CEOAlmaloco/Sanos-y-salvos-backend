package com.javadiseno.sanosysalvos.matching.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadiseno.sanosysalvos.matching.dtos.BusEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventBridgePublisher {

    private final EventBridgeClient eventBridgeClient;
    private final ObjectMapper objectMapper;

    @Value("${sanos.eventbridge.bus-name}")
    private String busName;

    public void publishMatchFound(BusEventDTO.EventDetailDTO detail) {
        try {
            String detailJson = objectMapper.writeValueAsString(detail);

            PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                    .eventBusName(busName)
                    .source("com.sanosysalvos.matching")
                    .detailType("match_found")
                    .detail(detailJson)
                    .build();

            eventBridgeClient.putEvents(PutEventsRequest.builder()
                    .entries(entry)
                    .build());

            log.info("Evento match_found publicado: reportId={} matchedReportId={} score={}",
                    detail.getReportId(), detail.getMatchedReportId(), detail.getMatchScore());

        } catch (Exception e) {
            log.error("Error publicando match_found: {}", e.getMessage());
        }
    }
}
