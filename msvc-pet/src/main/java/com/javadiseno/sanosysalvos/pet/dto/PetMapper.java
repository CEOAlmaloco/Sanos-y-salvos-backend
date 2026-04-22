package com.javadiseno.sanosysalvos.pet.dto;

import com.javadiseno.sanosysalvos.pet.models.PetModel;

import java.util.List;

public final class PetMapper {

    private PetMapper() {}

    public static PetResponse toResponse(PetModel e) {
        if (e == null) {
            return null;
        }
        PetResponse r = new PetResponse();
        r.setId(e.getId());
        r.setOwnerUserId(e.getOwnerUserId());
        r.setName(e.getName());
        r.setSpecies(e.getSpecies());
        r.setBreed(e.getBreed());
        r.setColor(e.getColor());
        r.setSize(e.getSize());
        r.setStatus(e.getStatus());
        r.setMicrochipNumber(e.getMicrochipNumber());
        r.setPrimaryPhotoMediaId(e.getPrimaryPhotoMediaId());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }

    public static List<PetResponse> toResponseList(List<PetModel> list) {
        return list.stream().map(PetMapper::toResponse).toList();
    }

    public static PetModel fromCreate(PetCreateRequest req) {
        return PetModel.builder()
                .ownerUserId(req.getOwnerUserId())
                .name(req.getName())
                .species(req.getSpecies())
                .breed(req.getBreed())
                .color(req.getColor())
                .size(req.getSize())
                .status(req.getStatus())
                .microchipNumber(req.getMicrochipNumber())
                .primaryPhotoMediaId(req.getPrimaryPhotoMediaId())
                .build();
    }

    /** Solo campos no nulos (actualización parcial). */
    public static PetModel fromUpdate(PetUpdateRequest req) {
        PetModel p = new PetModel();
        if (req.getOwnerUserId() != null) {
            p.setOwnerUserId(req.getOwnerUserId());
        }
        if (req.getName() != null) {
            p.setName(req.getName());
        }
        if (req.getSpecies() != null) {
            p.setSpecies(req.getSpecies());
        }
        if (req.getBreed() != null) {
            p.setBreed(req.getBreed());
        }
        if (req.getColor() != null) {
            p.setColor(req.getColor());
        }
        if (req.getSize() != null) {
            p.setSize(req.getSize());
        }
        if (req.getStatus() != null) {
            p.setStatus(req.getStatus());
        }
        if (req.getMicrochipNumber() != null) {
            p.setMicrochipNumber(req.getMicrochipNumber());
        }
        if (req.getPrimaryPhotoMediaId() != null) {
            p.setPrimaryPhotoMediaId(req.getPrimaryPhotoMediaId());
        }
        return p;
    }
}
