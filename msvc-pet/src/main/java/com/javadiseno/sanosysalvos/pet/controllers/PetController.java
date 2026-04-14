package com.javadiseno.sanosysalvos.pet.controllers;

import com.javadiseno.sanosysalvos.pet.models.PetModel;
import com.javadiseno.sanosysalvos.pet.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("/{petId}")
    public PetModel getById(@PathVariable UUID petId) {
        return petService.getById(petId);
    }

    /** actualizacion parcial de una mascota */
    @PutMapping("/{petId}")
    public PetModel update(@PathVariable UUID petId, @RequestBody PetModel body) {
        return petService.updatePet(petId, body);
    }
}
