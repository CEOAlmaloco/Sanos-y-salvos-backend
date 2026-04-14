package com.javadiseno.sanosysalvos.report.services;

import com.javadiseno.sanosysalvos.report.models.ReportMedia;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaService {

    Optional<ReportMedia> findById(UUID id);

    List<ReportMedia> findByReportId(UUID reportId);

    List<ReportMedia> findByReportIdOrderBySortOrderAscCreatedAtAsc(UUID reportId);

    long countByReportId(UUID reportId);

    void deleteByReportId(UUID reportId);

    ReportMedia save(ReportMedia media);

    void deleteById(UUID id);
}
