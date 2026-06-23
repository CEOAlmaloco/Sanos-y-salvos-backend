package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.dtos.ReportMapper;
import com.javadiseno.sanosysalvos.report.dtos.ReportResponse;
import com.javadiseno.sanosysalvos.report.dtos.requests.CreateReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.PatchReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.ResolveReportRequest;
import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.security.ReportJwtPrincipal;
import com.javadiseno.sanosysalvos.report.services.ReportService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    public Page<ReportResponse> list(@PageableDefault(size = 20) Pageable pageable) {
        return reportService.findAll(pageable).map(ReportMapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponse create(
            @Valid @RequestBody CreateReportRequest body,
            @RequestHeader(value = "X-User-Id", required = false) UUID userIdFromHeader,
            Authentication authentication) {
        UUID reporter = resolveReporterUserId(body, userIdFromHeader, authentication);
        ReportModel entity = ReportMapper.toNewEntity(body, reporter);
        return ReportMapper.toResponse(reportService.save(entity));
    }

    /** Reporter efectivo = usuario del JWT; body/header deben coincidir si se envían. */
    private static UUID resolveReporterUserId(
            CreateReportRequest body, UUID userIdFromHeader, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof ReportJwtPrincipal p)) {
            throw new ReportException("Autenticación JWT requerida para crear reporte");
        }
        UUID fromToken = p.getUserId();
        if (body.getReporterUserId() != null && !body.getReporterUserId().equals(fromToken)) {
            throw new ReportException("reporterUserId no coincide con el usuario del token");
        }
        if (userIdFromHeader != null && !userIdFromHeader.equals(fromToken)) {
            throw new ReportException("X-User-Id no coincide con el usuario del token");
        }
        return fromToken;
    }

    @GetMapping("/{reportId}")
    public ReportResponse getById(@PathVariable UUID reportId) {
        ReportModel report = reportService
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
        return ReportMapper.toResponse(report);
    }

    @PatchMapping("/{reportId}")
    public ReportResponse patch(
            @PathVariable UUID reportId, @Valid @RequestBody PatchReportRequest body) {
        return ReportMapper.toResponse(reportService.updateReport(reportId, body));
    }

    @PostMapping("/{reportId}/resolve")
    public ReportResponse resolve(
            @PathVariable UUID reportId,
            @Valid @RequestBody(required = false) ResolveReportRequest body) {
        return ReportMapper.toResponse(
                reportService.resolveReport(reportId, body != null ? body : new ResolveReportRequest()));
    }

    @PatchMapping("/{reportId}/close")
    public ReportResponse close(@PathVariable UUID reportId) {
        return ReportMapper.toResponse(reportService.closeReport(reportId));
    }

    @DeleteMapping("/{reportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID reportId) {
        reportService.deleteById(reportId);
    }
}
