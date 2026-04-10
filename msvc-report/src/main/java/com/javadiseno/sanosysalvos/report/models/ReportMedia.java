package com.javadiseno.sanosysalvos.report.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Enlace a un recurso multimedia (URL) asociado a un reporte.
 */
@Entity
@Table(name = "report_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMedia {

    @Id
    @GeneratedValue
    private UUID id;

    @JsonIgnore //para evitar json muy grandes y evitar recursividad
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //no lo carga de inmediato en la bd sino que lo carga cuando se necesita
    @JoinColumn(name = "report_id", nullable = false)
    private ReportModel report;//muchos comentarios pertenecen a un reporte

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();//evita olvidar la fecha de creación manualmente
        }
    }
}
