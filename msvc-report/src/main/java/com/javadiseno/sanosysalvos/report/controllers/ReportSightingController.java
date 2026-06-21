package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.dtos.ReportMapper;
import com.javadiseno.sanosysalvos.report.dtos.SightingResponse;
import com.javadiseno.sanosysalvos.report.dtos.requests.CreateSightingRequest;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.models.ReportSighting;
import com.javadiseno.sanosysalvos.report.services.ReportService;
import com.javadiseno.sanosysalvos.report.services.SightingService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * avistamientos asociados a un reporte
 */
@RestController
@RequestMapping("/api/v1/reports/{reportId}/sightings")
@RequiredArgsConstructor
public class ReportSightingController {

    private final ReportService reportService;
    private final SightingService sightingService;

    @GetMapping
    public List<SightingResponse> list(@PathVariable UUID reportId) {
        ensureReportExists(reportId);
        return ReportMapper.toSightingResponseList(
                sightingService.findByReportIdOrderBySpottedAtDesc(reportId));
    }

    @GetMapping("/{sightingId}")
    public SightingResponse getOne(@PathVariable UUID reportId, @PathVariable UUID sightingId) {
        ensureReportExists(reportId);
        ReportSighting s = sightingService
                .findById(sightingId)
                .orElseThrow(() -> new ResourceNotFoundException("Avistamiento no encontrado: " + sightingId));
        if (!reportId.equals(s.getReport().getId())) {
            throw new ResourceNotFoundException("Avistamiento no pertenece a este reporte");
        }
        return ReportMapper.toSightingResponse(s);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SightingResponse create(
            @PathVariable UUID reportId, @Valid @RequestBody CreateSightingRequest body) {
        ReportModel report = reportService
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
        ReportSighting sighting = ReportMapper.toNewSighting(body, report);
        return ReportMapper.toSightingResponse(sightingService.save(sighting));
    }

    @DeleteMapping("/{sightingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID reportId, @PathVariable UUID sightingId) {
        getOne(reportId, sightingId);
        sightingService.deleteById(sightingId);
    }

    private void ensureReportExists(UUID reportId) {
        if (reportService.findById(reportId).isEmpty()) {
            throw new ResourceNotFoundException("Report no encontrado: " + reportId);
        }
    }
}
