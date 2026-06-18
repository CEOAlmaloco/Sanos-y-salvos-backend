package com.javadiseno.sanosysalvos.matching.services;

import com.javadiseno.sanosysalvos.matching.dtos.BusEventDTO;

public interface MatchingService {

    void processEvent(BusEventDTO busEventDTO);
}
