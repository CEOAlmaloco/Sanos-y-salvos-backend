package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.services.ReportService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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

    //el me es el usuario que esta autenticado TODO _: hay q cambiarlo por el jwt
    @GetMapping("/me/reports")
    public List<ReportModel> myReports(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId) {
        if (userId == null) {
            throw new ReportException("Cabecera X-User-Id requerida hasta integrar JWT");
        }
        return reportService.findByReporterUserIdOrderByCreatedAtDesc(userId);
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
