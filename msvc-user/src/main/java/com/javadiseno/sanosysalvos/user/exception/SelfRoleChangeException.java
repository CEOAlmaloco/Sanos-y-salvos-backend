package com.javadiseno.sanosysalvos.user.exception;

public class SelfRoleChangeException extends RuntimeException{

    public SelfRoleChangeException(){
        super("No puedes cambiar tu propio rol");
    }
}
