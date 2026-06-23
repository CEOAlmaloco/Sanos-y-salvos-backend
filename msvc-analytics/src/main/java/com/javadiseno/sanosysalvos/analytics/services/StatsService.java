package com.javadiseno.sanosysalvos.analytics.services;

import com.javadiseno.sanosysalvos.analytics.dtos.StatsResponseDTO;

public interface StatsService {
    StatsResponseDTO getGeneralStats();
    StatsResponseDTO getStatsByDateRange(String fromDate, String toDate);
}
