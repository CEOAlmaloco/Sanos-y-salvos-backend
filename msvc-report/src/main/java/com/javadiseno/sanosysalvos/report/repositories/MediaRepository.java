package com.javadiseno.sanosysalvos.report.repositories;

import com.javadiseno.sanosysalvos.report.models.ReportMedia;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
  Poner texto luego
 */
@Repository
public interface MediaRepository extends JpaRepository<ReportMedia, UUID> {

    List<ReportMedia> findByReport_Id(UUID reportId);

    List<ReportMedia> findByReport_IdOrderBySortOrderAscCreatedAtAsc(UUID reportId);

    long countByReport_Id(UUID reportId);

    void deleteByReport_Id(UUID reportId);
}
