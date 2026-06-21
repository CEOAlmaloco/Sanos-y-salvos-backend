package com.javadiseno.sanosysalvos.pet.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class) //este sirve para crear las columnas created_at y updated_at automaticamente
@Entity
@Table(
        name = "pets",
        schema = "pets_schema",
        indexes = {
                @Index(name = "idx_pets_owner_user_id", columnList = "owner_user_id"),
                @Index(name = "idx_pets_status", columnList = "status"),
                @Index(name = "idx_pets_microchip", columnList = "microchip_number")
        }
) 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetModel {

    @Id
    @Column(name = "pet_id", nullable = false, updatable = false)
    private UUID id;

    /**
     * usuario que registro la mascota, puede ser null o encontrado sin dueño
     */
    @Column(name = "owner_user_id")
    private UUID ownerUserId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "species", length = 80)
    private String species;

    @Column(name = "breed", length = 80)
    private String breed;

    @Column(name = "color", length = 60)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", length = 20)
    private PetSize size;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PetStatus status = PetStatus.LOST;

    /**
     * Chip se identificara cmo esto
     */
    @Column(name = "microchip_number", unique = true, length = 64)
    private String microchipNumber;

    /**
     * foto portada
     */
    @Column(name = "primary_photo_media_id")
    private UUID primaryPhotoMediaId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void assignId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = PetStatus.LOST;
        }
        if (this.size == null) {
            this.size = PetSize.UNKNOWN;
        }
    }
}
