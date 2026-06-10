package com.javadiseno.sanosysalvos.analytics.services;

import com.javadiseno.sanosysalvos.analytics.dtos.HotZoneDTO;

import java.util.List;

public interface HotZoneService {
    List<HotZoneDTO> getHotZones();
}
