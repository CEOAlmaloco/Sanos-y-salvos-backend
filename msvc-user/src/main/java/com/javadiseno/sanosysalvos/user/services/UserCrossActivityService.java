package com.javadiseno.sanosysalvos.user.services;

import com.javadiseno.sanosysalvos.user.client.PetServiceClient;
import com.javadiseno.sanosysalvos.user.client.ReportServiceClient;
import com.javadiseno.sanosysalvos.user.client.dtos.UserActivitySummaryResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * este service sirve para obtener la actividad de un usuario en los servicios de pet y report 
 * se implemento ya que 
 * 
 */
@Service
@RequiredArgsConstructor
public class UserCrossActivityService {

    private final PetServiceClient petServiceClient;
    private final ReportServiceClient reportServiceClient;

    public UserActivitySummaryResponse activityCounts(UUID userId) {
        var pets = petServiceClient.listPetsForOwner(userId, userId);
        var reports = reportServiceClient.listReportsForUser(userId);
        int pc = pets != null ? pets.size() : 0;
        int rc = reports != null ? reports.size() : 0;
        return new UserActivitySummaryResponse(userId, pc, rc);
    }
}
