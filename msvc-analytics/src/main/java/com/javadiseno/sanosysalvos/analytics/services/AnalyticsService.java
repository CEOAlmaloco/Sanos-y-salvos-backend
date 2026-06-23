package com.javadiseno.sanosysalvos.analytics.services;

import com.javadiseno.sanosysalvos.analytics.dtos.BusEventDTO;

public interface AnalyticsService {

	void processEvent(BusEventDTO busEventDTO);
}
