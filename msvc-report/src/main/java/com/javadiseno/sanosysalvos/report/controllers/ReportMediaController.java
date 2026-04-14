package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportMedia;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.services.MediaService;
import com.javadiseno.sanosysalvos.report.services.ReportService;
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

@RestController
@RequestMapping("/api/v1/reports/{reportId}/media")
@RequiredArgsConstructor
public class ReportMediaController {

    private final ReportService reportService;
    private final MediaService mediaService;

    @GetMapping
    public List<ReportMedia> list(@PathVariable UUID reportId) {
        ensureReportExists(reportId);
        return mediaService.findByReportIdOrderBySortOrderAscCreatedAtAsc(reportId);
    }

    @GetMapping("/{mediaId}")
    public ReportMedia getOne(@PathVariable UUID reportId, @PathVariable UUID mediaId) {
        ensureReportExists(reportId);
        ReportMedia m = mediaService
                .findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Medio no encontrado: " + mediaId));
        if (!reportId.equals(m.getReport().getId())) {
            throw new ResourceNotFoundException("El medio no pertenece a este reporte");
        }
        return m;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportMedia create(@PathVariable UUID reportId, @RequestBody ReportMedia body) {
        ReportModel report = reportService
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
        body.setReport(report);
        return mediaService.save(body);
    }


    @DeleteMapping("/{mediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID reportId, @PathVariable UUID mediaId) {
        getOne(reportId, mediaId);
        mediaService.deleteById(mediaId);
    }

    //verificar que el reporte existe para no crear un medio sin reporte
    private void ensureReportExists(UUID reportId) {
        if (reportService.findById(reportId).isEmpty()) {
            throw new ResourceNotFoundException("Report no encontrado: " + reportId);
        }
    }
}
