package com.javadiseno.sanosysalvos.report.services;

import com.javadiseno.sanosysalvos.report.dtos.requests.PatchReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.ResolveReportRequest;
import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.mapping.ReportApiMapper;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.models.ReportModel.ReportStatus;
import com.javadiseno.sanosysalvos.report.repositories.ReportRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 esta logica de negocio tiene 
 los enlaces a mascota y reportante, y tipo de reporte
 
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ReportModel> findAll(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportModel> findById(UUID id) {
        return reportRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportModel> findByPetId(UUID petId) {
        return reportRepository.findByPetId(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportModel> findByPetId(UUID petId, Pageable pageable) {
        return reportRepository.findByPetId(petId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportModel> findByPetIdOrderByReportedAtDesc(UUID petId) {
        return reportRepository.findByPetIdOrderByReportedAtDesc(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportModel> findByReporterUserIdOrderByCreatedAtDesc(UUID reporterUserId) {
        return reportRepository.findByReporterUserIdOrderByCreatedAtDesc(reporterUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportModel> findWithSightingsById(UUID id) {
        return reportRepository.findWithSightingsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportModel> findWithMediaById(UUID id) {
        return reportRepository.findWithMediaById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdAndReporterUserId(UUID reportId, UUID reporterUserId) {
        return reportRepository.existsByIdAndReporterUserId(reportId, reporterUserId);
    }

    @Override
    @Transactional
    public ReportModel save(ReportModel report) {
        validateMandatoryFields(report);
        if (report.getReportedAt() == null) {
            report.setReportedAt(Instant.now());
        }
        if (report.getStatus() == null) {
            report.setStatus(ReportStatus.ACTIVE);
        }
        return reportRepository.save(report);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report no encontrado: " + id);
        }
        reportRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ReportModel closeReport(UUID reportId) {
        return resolveReport(reportId, new ResolveReportRequest());
    }

    @Override
    @Transactional
    public ReportModel updateReport(UUID reportId, PatchReportRequest patch) {
        ReportModel report = reportRepository
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
        ReportApiMapper.applyPatch(report, patch);
        return reportRepository.save(report);
    }

    @Override
    @Transactional
    public ReportModel resolveReport(UUID reportId, ResolveReportRequest request) {
        ReportModel report = reportRepository
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report no encontrado: " + reportId));
        if (report.getStatus() == ReportStatus.CLOSED) {
            throw new ReportException("Conflicto: el reporte ya está cerrado");
        }
        report.setStatus(ReportStatus.CLOSED);
        report.setResolvedAt(Instant.now());
        if (request != null && request.getNota() != null && !request.getNota().isBlank()) {
            String tag = request.getMotivo() != null ? "[" + request.getMotivo() + "] " : "[Cierre] ";
            String prev = report.getDescription() != null ? report.getDescription() + "\n" : "";
            report.setDescription(prev + tag + request.getNota());
        }
        return reportRepository.save(report);
    }

    /**(SY-20): enlaces a mascota y reportante, y tipo de reporte. */
    private static void validateMandatoryFields(ReportModel report) {
        if (report.getPetId() == null) {
            throw new ReportException("petId es obligatorio");
        }
        if (report.getReporterUserId() == null) {
            throw new ReportException("reporterUserId es obligatorio");
        }
        if (report.getType() == null) {
            throw new ReportException("type es obligatorio");
        }
    }
}
