package com.javadiseno.sanosysalvos.report.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reporte de mascota (perdido / encontrado / avistamiento).
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportModel {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "reporter_user_id", nullable = false)
    private UUID reporterUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReportType type;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Column(name = "location_description", length = 2000)
    private String locationDescription;

    @Column(name = "reported_at", nullable = false)
    private Instant reportedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReportStatus status;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportSighting> sightings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportMedia> mediaLinks = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (reportedAt == null) {
            reportedAt = createdAt;
        }
        if (status == null) {
            status = ReportStatus.ACTIVE;
        }
    }

    public enum ReportType {
        LOST,
        FOUND,
        SIGHTING
    }

    public enum ReportStatus {
        ACTIVE,
        CLOSED
    }
}
