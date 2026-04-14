package com.javadiseno.sanosysalvos.report.services;

import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportSighting;
import com.javadiseno.sanosysalvos.report.repositories.SightingRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Avistamientos (SY-26): deben pertenecer a un reporte.
 */
@Service
@RequiredArgsConstructor
public class SightingServiceImpl implements SightingService {

    private final SightingRepository sightingRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportSighting> findById(UUID id) {
        return sightingRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSighting> findByReportId(UUID reportId) {
        return sightingRepository.findByReport_Id(reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSighting> findByReportId(UUID reportId, Sort sort) {
        return sightingRepository.findByReport_Id(reportId, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSighting> findByReportIdOrderBySpottedAtDesc(UUID reportId) {
        return sightingRepository.findByReport_IdOrderBySpottedAtDesc(reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByReportId(UUID reportId) {
        return sightingRepository.countByReport_Id(reportId);
    }

    @Override
    @Transactional
    public void deleteByReportId(UUID reportId) {
        sightingRepository.deleteByReport_Id(reportId);
    }

    @Override
    @Transactional
    public ReportSighting save(ReportSighting sighting) {
        if (sighting.getReport() == null) {
            throw new ReportException("El avistamiento debe tener un report asociado");
        }
        return sightingRepository.save(sighting);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!sightingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avistamiento no encontrado: " + id);
        }
        sightingRepository.deleteById(id);
    }
}
