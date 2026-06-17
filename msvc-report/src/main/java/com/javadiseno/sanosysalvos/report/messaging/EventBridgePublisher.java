package com.javadiseno.sanosysalvos.report.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadiseno.sanosysalvos.report.dtos.ReportEventDTO;
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

    public void publish(ReportEventDTO reportEventDTO) {
        try {
            String detailJson = objectMapper.writeValueAsString(reportEventDTO.getDetail());

            PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                    .eventBusName(busName)
                    .source(reportEventDTO.getSource())
                    .detailType(reportEventDTO.getDetailType())
                    .detail(detailJson)
                    .build();

            eventBridgeClient.putEvents(PutEventsRequest.builder()
                    .entries(entry)
                    .build());

            log.info("Evento publicado a EventBridge: type={} reportId={}",
                    reportEventDTO.getDetailType(),
                    reportEventDTO.getDetail() != null ? reportEventDTO.getDetail().getReportId() : "null");

        } catch (Exception e) {
            log.error("Error publicando evento a EventBridge: type={} error={}",
                    reportEventDTO.getDetailType(), e.getMessage());
        }
    }
}
