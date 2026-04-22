package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.security.ReportJwtPrincipal;
import com.javadiseno.sanosysalvos.report.services.ReportService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * reportes asociados a un usuario
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserReportController {

    private final ReportService reportService;

    /** Requiere {@code Authorization: Bearer &lt;JWT&gt;} (subject = userId). */
    @GetMapping("/me/reports")
    public List<ReportModel> myReports(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof ReportJwtPrincipal p)) {
            throw new ReportException("Autenticación JWT requerida para /me/reports");
        }
        return reportService.findByReporterUserIdOrderByCreatedAtDesc(p.getUserId());
    }

    //lista de reportes de un usuario
    @GetMapping("/{userId}/reports")
    public List<ReportModel> listByUser(@PathVariable UUID userId) {
        return reportService.findByReporterUserIdOrderByCreatedAtDesc(userId);
    }

    //obtener un reporte de un usuario
    @GetMapping("/{userId}/reports/{reportId}")
    public ReportModel getByUserAndReport(
            @PathVariable UUID userId, @PathVariable UUID reportId) {
        ReportModel r = reportService
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
        if (!userId.equals(r.getReporterUserId())) {
            throw new ResourceNotFoundException("Report no encontrado para este usuario");
        }
        return r;
    }
}
