package com.javadiseno.sanosysalvos.report.services;

import com.javadiseno.sanosysalvos.report.models.ReportSighting;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;

public interface SightingService {

    Optional<ReportSighting> findById(UUID id);

    List<ReportSighting> findByReportId(UUID reportId);

    List<ReportSighting> findByReportId(UUID reportId, Sort sort);

    List<ReportSighting> findByReportIdOrderBySpottedAtDesc(UUID reportId);

    long countByReportId(UUID reportId);

    void deleteByReportId(UUID reportId);

    ReportSighting save(ReportSighting sighting);

    void deleteById(UUID id);
}
