package com.javadiseno.sanosysalvos.notification.services;

import com.javadiseno.sanosysalvos.notification.dtos.BusEventDTO;

public interface NotificationService {

    void processEvent(BusEventDTO busEventDTO);
}
