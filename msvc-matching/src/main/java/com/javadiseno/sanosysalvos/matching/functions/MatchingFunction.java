package com.javadiseno.sanosysalvos.matching.functions;

import com.javadiseno.sanosysalvos.matching.dtos.BusEventDTO;
import com.javadiseno.sanosysalvos.matching.services.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component("matchingFunction")
@RequiredArgsConstructor
public class MatchingFunction implements Function<BusEventDTO, String> {

    private final MatchingService matchingService;

    @Override
    public String apply(BusEventDTO busEventDTO) {
        try {
            matchingService.processEvent(busEventDTO);
            return "OK";
        } catch (Exception e) {
            log.error("Error en matchingFunction: {}", e.getMessage(), e);
            return "ERROR";
        }
    }
}
