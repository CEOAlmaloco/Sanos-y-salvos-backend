package com.javadiseno.sanosysalvos.analytics.services;

// SY-73 Servicio que genera estadísticas generales desde DynamoDB

import com.javadiseno.sanosysalvos.analytics.dtos.StatsResponseDTO;
import com.javadiseno.sanosysalvos.analytics.models.AnalyticsMetric;
import com.javadiseno.sanosysalvos.analytics.repositories.AnalyticsMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// SY-73 Servicio que genera estadísticas generales desde DynamoDB
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final AnalyticsMetricRepository analyticsMetricRepository;

    // Devuelve estadísticas generales del sistema
    @Override
    public StatsResponseDTO getGeneralStats(){
        log.info("Calculando estadísticas generales desde DynamoDB");

        long totalReports = countByEventType("pet_reported");
        long totalFound = countByEventType("pet_found");
        long totalMatches = countByEventType("match_found");

        return StatsResponseDTO.builder()
                .totalReports(totalReports)
                .totalPetsFound(totalFound)
                .totalMatches(totalMatches)
                .build();
    }

    @Override
    public StatsResponseDTO getStatsByDateRange(String fromDate, String toDate){
        log.info("Calculando estadísticas por rango: {} -> {}", fromDate, toDate);

        long totalReports = countByEventTypeAndDateRange("pet_reported", fromDate, toDate);
        long totalFound = countByEventTypeAndDateRange("pet_found", fromDate, toDate);
        long totalMatches = countByEventTypeAndDateRange("match_found", fromDate, toDate);

        return StatsResponseDTO.builder()
                .totalReports(totalReports)
                .totalPetsFound(totalFound)
                .totalMatches(totalMatches)
                .build();
    }

    private long countByEventType(String eventType){
        List<AnalyticsMetric> metrics = analyticsMetricRepository.findByEventType(eventType);
        return metrics.size();
    }

    private long countByEventTypeAndDateRange(String eventType, String from, String to){
        List<AnalyticsMetric> metrics = analyticsMetricRepository.findByEventTypeAndDateRange(eventType, from, to);
        return metrics.size();
    }
}
