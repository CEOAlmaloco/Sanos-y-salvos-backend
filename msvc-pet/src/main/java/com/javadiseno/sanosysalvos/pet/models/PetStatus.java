package com.javadiseno.sanosysalvos.pet.models;

//*
//estado lost cuando se reporta como perdido
//estado found cuando se encuentra
//estado recovered cuando se recupera, luego se cierra el caso
//*
public enum PetStatus {
    LOST, 
    FOUND, 
    RECOVERED 
}
