package com.javadiseno.sanosysalvos.report.repositories;

import com.javadiseno.sanosysalvos.report.models.ReportSighting;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
  avistamientos asociados a un reporte (id del reporte)
 */
@Repository
public interface SightingRepository extends JpaRepository<ReportSighting, UUID> {

    List<ReportSighting> findByReport_Id(UUID reportId);

    List<ReportSighting> findByReport_Id(UUID reportId, Sort sort);

    List<ReportSighting> findByReport_IdOrderBySpottedAtDesc(UUID reportId);

    long countByReport_Id(UUID reportId);

    void deleteByReport_Id(UUID reportId);
}
