package com.javadiseno.sanosysalvos.analytics.services;

import com.javadiseno.sanosysalvos.analytics.config.GeohashUtil;
import com.javadiseno.sanosysalvos.analytics.dtos.HotZoneDTO;
import com.javadiseno.sanosysalvos.analytics.models.AnalyticsMetric;
import com.javadiseno.sanosysalvos.analytics.repositories.AnalyticsMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// SY-72 Servicio que calcula zonas calientes y cachea en Redis
@Slf4j
@Service
@RequiredArgsConstructor
public class HotZoneServiceImpl implements HotZoneService {

    private final AnalyticsMetricRepository analyticsMetricRepository;


    private static final String CACHE_KEY = "analytics:hot-zones";

    @Override
    public List<HotZoneDTO> getHotZones() {

        // 1. Calcular desde DynamoDB
        log.info("Calculando hot zones desde DynamoDB");
        return calculateHotZones();
    }

    private List<HotZoneDTO> calculateHotZones() {

        // Agrupar métricas de todos los tipos de evento por geohash
        Map<String, Map<String, Long>> zoneCounters = new HashMap<>();

        for (String eventType : List.of("pet_reported", "pet_found", "match_found")) {
            List<AnalyticsMetric> metrics = analyticsMetricRepository.findByEventType(eventType);

            for (AnalyticsMetric metric : metrics) {
                if (metric.getLatitude() == null || metric.getLongitude() == null) continue;

                String geohash = GeohashUtil.encode(metric.getLatitude(), metric.getLongitude());
                zoneCounters.computeIfAbsent(geohash, k -> new HashMap<>())
                        .merge(eventType, 1L, Long::sum);
            }
        }

        // Convertir a DTOs
        return zoneCounters.entrySet().stream()
                .map(entry -> {
                    String geohash = entry.getKey();
                    Map<String, Long> counts = entry.getValue();
                    double[] center = GeohashUtil.decode(geohash);

                    long petReported  = counts.getOrDefault("pet_reported",  0L);
                    long petFound     = counts.getOrDefault("pet_found",     0L);
                    long matchFound   = counts.getOrDefault("match_found",   0L);

                    return HotZoneDTO.builder()
                            .geohash(geohash)
                            .latitude(center[0])
                            .longitude(center[1])
                            .totalEvents(petReported + petFound + matchFound)
                            .petReportedCount(petReported)
                            .petFoundCount(petFound)
                            .matchFoundCount(matchFound)
                            .build();
                })
                // Ordenar de mayor a menor por total de eventos
                .sorted(Comparator.comparingLong(HotZoneDTO::getTotalEvents).reversed())
                .collect(Collectors.toList());
    }
}
