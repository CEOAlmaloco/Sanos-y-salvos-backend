package com.javadiseno.sanosysalvos.integration.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

// SY-40 Modelo que representa la credencial de acceso para organismos externos

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(
    name = "institution_api_keys",
    schema = "integrations_schema",
    uniqueConstraints = {
            @UniqueConstraint(name = "uk_institution_api_key", columnNames = "api_key"),
            @UniqueConstraint(name = "uk_institution_user_id", columnNames = "user_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionApiKey {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @PrePersist
    private void prePersist(){
        if(this.id == null){
            this.id = UUID.randomUUID();
        }
    }

    @Column(name = "institution_name", nullable = false, length = 125)
    private String institutionName;

    @Column(name = "api_key", nullable = false, length = 250)
    private String apiKey;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
