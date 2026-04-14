package com.javadiseno.sanosysalvos.report.services;

import com.javadiseno.sanosysalvos.report.dtos.requests.PatchReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.ResolveReportRequest;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    Page<ReportModel> findAll(Pageable pageable);

    Optional<ReportModel> findById(UUID id);

    List<ReportModel> findByPetId(UUID petId);

    Page<ReportModel> findByPetId(UUID petId, Pageable pageable);

    List<ReportModel> findByPetIdOrderByReportedAtDesc(UUID petId);

    List<ReportModel> findByReporterUserIdOrderByCreatedAtDesc(UUID reporterUserId);

    Optional<ReportModel> findWithSightingsById(UUID id);

    Optional<ReportModel> findWithMediaById(UUID id);

    boolean existsByIdAndReporterUserId(UUID reportId, UUID reporterUserId);

    ReportModel save(ReportModel report);

    void deleteById(UUID id);

    /** Marca el reporte como cerrado y fija {@code resolved_at} */
    ReportModel closeReport(UUID reportId);

    ReportModel updateReport(UUID reportId, PatchReportRequest patch);

    /** Cierra el reporte; opcionalmente anexa motivo/nota (wiki POST .../resolve). */
    ReportModel resolveReport(UUID reportId, ResolveReportRequest request);
}
