package com.javadiseno.sanosysalvos.analytics.services;

import com.javadiseno.sanosysalvos.analytics.dtos.BusEventDTO;
import com.javadiseno.sanosysalvos.analytics.dtos.EventType;
import com.javadiseno.sanosysalvos.analytics.models.AnalyticsMetric;
import com.javadiseno.sanosysalvos.analytics.repositories.AnalyticsMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// SY-70 | SY-71 Servicio que transforma un evento
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService{

    private final AnalyticsMetricRepository analyticsMetricRepository;

    @Value("${analytics.dynamodb.ttl-days:365}")
    private long ttlDays;

    @Override
    public void processEvent(BusEventDTO busEventDTO) {
        if (busEventDTO == null || busEventDTO.getDetailType() == null) {
            log.warn("Evento nulo. Sera ignorado");
            return;
        }

        // Validar que sea un tipo conocido
        try {
            EventType.valueOf(busEventDTO.getDetailType());
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de evento desconocido: {} — ignorado", busEventDTO.getDetailType());
            return;
        }

        log.info("Procesando evento: type={} source={}", busEventDTO.getDetailType(), busEventDTO.getSource());

        AnalyticsMetric metric = buildMetric(busEventDTO);
        analyticsMetricRepository.save(metric);

        log.info("Métrica guardada: pk={} sk={}", metric.getPk(), metric.getSk());
    }

    private AnalyticsMetric buildMetric(BusEventDTO busEventDTO) {
        String eventType = busEventDTO.getDetailType();
        String today = LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String uuid = UUID.randomUUID().toString();
        Instant now = Instant.now();

        BusEventDTO.EventDetailDTO detail = busEventDTO.getDetail();

        return AnalyticsMetric.builder()
                .pk("EVENTO#" + eventType)
                .sk("FECHA#" + today + "#" + uuid)
                .eventType(eventType)
                .reportId(detail != null && detail.getReportId() != null ? detail.getReportId().toString() : null)
                .petId(detail != null && detail.getPetId() != null ? detail.getPetId().toString() : null)
                .userId(detail != null && detail.getUserId() != null ? detail.getUserId().toString() : null)
                .latitude(detail != null ? detail.getLatitude() : null)
                .longitude(detail != null ? detail.getLongitude() : null )
                .eventDate(detail != null && detail.getEventDate() != null ? detail.getEventDate().toString() : now.toString())
                .createdAt(now.toString())
                .ttl(now.getEpochSecond() + (ttlDays * 86400))
                .build();
    }
}
