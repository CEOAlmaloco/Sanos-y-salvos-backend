package com.javadiseno.sanosysalvos.user.exceptions;

public class SelfRoleChangeException extends RuntimeException{

    public SelfRoleChangeException(){
        super("No puedes cambiar tu propio rol");
    }
}
