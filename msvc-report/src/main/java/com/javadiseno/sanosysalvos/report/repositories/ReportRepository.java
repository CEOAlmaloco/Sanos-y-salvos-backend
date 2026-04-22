package com.javadiseno.sanosysalvos.report.repositories;

import com.javadiseno.sanosysalvos.report.models.ReportModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
  reportes de mascotas perdidas, encontradas o vistas.
 */
@Repository
public interface ReportRepository extends JpaRepository<ReportModel, UUID> {

    /** Columna BD {@code pet_id} → propiedad {@code petId}. */
    List<ReportModel> findByPetId(UUID petId);

    Page<ReportModel> findByPetId(UUID petId, Pageable pageable);

    List<ReportModel> findByPetIdOrderByReportedAtDesc(UUID petId);

    /** Columna BD {@code reporter_user_id} → propiedad {@code reporterUserId}. */
    List<ReportModel> findByReporterUserIdOrderByCreatedAtDesc(UUID reporterUserId);

    @EntityGraph(attributePaths = "sightings")
    @Query("SELECT r FROM ReportModel r WHERE r.id = :id")
    Optional<ReportModel> findWithSightingsById(@Param("id") UUID id);

    @EntityGraph(attributePaths = "mediaLinks")
    @Query("SELECT r FROM ReportModel r WHERE r.id = :id")
    Optional<ReportModel> findWithMediaById(@Param("id") UUID id);

    boolean existsByIdAndReporterUserId(UUID id, UUID reporterUserId);
}
