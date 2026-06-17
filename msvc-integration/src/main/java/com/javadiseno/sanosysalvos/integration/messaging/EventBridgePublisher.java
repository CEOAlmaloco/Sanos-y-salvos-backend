package com.javadiseno.sanosysalvos.integration.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadiseno.sanosysalvos.integration.dto.IntegrationEventDTO;
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

    public void publish(IntegrationEventDTO integrationEventDTO) {
        try {
            String detailJson = objectMapper.writeValueAsString(integrationEventDTO.getDetail());

            PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                    .eventBusName(busName)
                    .source(integrationEventDTO.getSource())
                    .detailType(integrationEventDTO.getDetailType())
                    .detail(detailJson)
                    .build();

            eventBridgeClient.putEvents(PutEventsRequest.builder()
                    .entries(entry)
                    .build());

            log.info("Evento publicado a EventBridge desde Integration: type={} reportId={}",
                    integrationEventDTO.getDetailType(),
                    integrationEventDTO.getDetail() != null ? integrationEventDTO.getDetail().getReportId() : "null");

        } catch (Exception e) {
            log.error("Error publicando evento a EventBridge desde Integration: type={} error={}",
                    integrationEventDTO.getDetailType(), e.getMessage());
        }
    }
}
