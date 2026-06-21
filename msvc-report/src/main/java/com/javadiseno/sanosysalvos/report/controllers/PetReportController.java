package com.javadiseno.sanosysalvos.report.controllers;

import com.javadiseno.sanosysalvos.report.dtos.ReportMapper;
import com.javadiseno.sanosysalvos.report.dtos.ReportResponse;
import com.javadiseno.sanosysalvos.report.services.ReportService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * reportes asociados a una mascota
 */
@RestController
@RequestMapping("/api/v1/pets/{petId}/reports")
@RequiredArgsConstructor
public class PetReportController {

    private final ReportService reportService;

    @GetMapping
    public Page<ReportResponse> page(
            @PathVariable UUID petId, @PageableDefault(size = 20) Pageable pageable) {
        return reportService.findByPetId(petId, pageable).map(ReportMapper::toResponse);
    }

    @GetMapping("/all")
    public List<ReportResponse> listAll(@PathVariable UUID petId) {
        return ReportMapper.toResponseList(reportService.findByPetIdOrderByReportedAtDesc(petId));
    }
}
