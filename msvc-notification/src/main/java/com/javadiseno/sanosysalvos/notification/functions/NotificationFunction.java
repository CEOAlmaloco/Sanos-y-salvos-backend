package com.javadiseno.sanosysalvos.notification.functions;

import com.javadiseno.sanosysalvos.notification.dtos.BusEventDTO;
import com.javadiseno.sanosysalvos.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component("notificationFunction")
@RequiredArgsConstructor
public class NotificationFunction implements Function<BusEventDTO, String> {

    private final NotificationService notificationService;

    @Override
    public String apply(BusEventDTO busEventDTO) {
        try {
            notificationService.processEvent(busEventDTO);
            return "OK";
        } catch (Exception e) {
            log.error("Error en notificationFunction: {}", e.getMessage(), e);
            return "ERROR";
        }
    }
}
