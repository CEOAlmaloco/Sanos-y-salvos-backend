package com.javadiseno.sanosysalvos.pet.controllers;

import com.javadiseno.sanosysalvos.pet.dto.PetCreateRequest;
import com.javadiseno.sanosysalvos.pet.dto.PetMapper;
import com.javadiseno.sanosysalvos.pet.dto.PetResponse;
import com.javadiseno.sanosysalvos.pet.dto.PetUpdateRequest;
import com.javadiseno.sanosysalvos.pet.exception.PetAccessDeniedException;
import com.javadiseno.sanosysalvos.pet.services.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<PetResponse> listByOwner(
            @RequestParam UUID ownerUserId,
            @RequestHeader(value = "X-User-Id", required = false) UUID actingUserId) {
        return PetMapper.toResponseList(petService.listPetsForOwner(ownerUserId, actingUserId));
    }

    @GetMapping("/{petId}")
    public PetResponse getById(@PathVariable UUID petId) {
        return PetMapper.toResponse(petService.getById(petId));
    }

    @PostMapping
    public ResponseEntity<PetResponse> create(
            @Valid @RequestBody PetCreateRequest body,
            @RequestHeader(value = "X-User-Id", required = false) UUID actingUserId) {
        if (body.getOwnerUserId() != null) {
            if (actingUserId == null) {
                throw new IllegalArgumentException("Cabecera X-User-Id requerida cuando se envía ownerUserId");
            }
            if (!body.getOwnerUserId().equals(actingUserId)) {
                throw new PetAccessDeniedException("ownerUserId debe coincidir con el usuario autenticado (X-User-Id)");
            }
        }
        var saved = petService.createPet(PetMapper.fromCreate(body));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(PetMapper.toResponse(saved));
    }

    @PutMapping("/{petId}")
    public PetResponse update(
            @PathVariable UUID petId,
            @Valid @RequestBody PetUpdateRequest body,
            @RequestHeader(value = "X-User-Id", required = false) UUID actingUserId) {
        return PetMapper.toResponse(petService.updatePet(petId, PetMapper.fromUpdate(body), actingUserId));
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID petId,
            @RequestHeader(value = "X-User-Id", required = false) UUID actingUserId) {
        petService.deletePetForOwner(petId, actingUserId);
    }
}
