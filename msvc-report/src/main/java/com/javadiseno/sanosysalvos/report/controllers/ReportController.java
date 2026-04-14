package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.dtos.requests.CreateReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.PatchReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.ResolveReportRequest;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.mapping.ReportApiMapper;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.services.ReportService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *  controlador principal de reportes (get, post, patch, delete) para pet,sighting y media 
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public Page<ReportModel> list(@PageableDefault(size = 20) Pageable pageable) {
        return reportService.findAll(pageable);
    }

    //TEMPORAL: permite obtener el usuario por el body o el header SIN JWT autenticado
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportModel create(
            @RequestBody CreateReportRequest body,
            @RequestHeader(value = "X-User-Id", required = false) UUID userIdFromHeader) {
        UUID reporter = body.getReporterUserId() != null ? body.getReporterUserId() : userIdFromHeader;
        ReportModel entity = ReportApiMapper.toNewEntity(body, reporter);
        return reportService.save(entity);
    }

    @GetMapping("/{reportId}")
    public ReportModel getById(@PathVariable UUID reportId) {
        return reportService
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
    }

    @PatchMapping("/{reportId}")
    public ReportModel patch(@PathVariable UUID reportId, @RequestBody PatchReportRequest body) {
        return reportService.updateReport(reportId, body);
    }

    //resolver un reporte osea confirmar que la mascota fue encontrada
    @PostMapping("/{reportId}/resolve")
    public ReportModel resolve(
            @PathVariable UUID reportId, @RequestBody(required = false) ResolveReportRequest body) {
        return reportService.resolveReport(reportId, body != null ? body : new ResolveReportRequest());
    }

    //cerrar un reporte da igual si fue resuelto o no
    @PatchMapping("/{reportId}/close")
    public ReportModel close(@PathVariable UUID reportId) {
        return reportService.closeReport(reportId);
    }

    @DeleteMapping("/{reportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID reportId) {
        reportService.deleteById(reportId);
    }
}
