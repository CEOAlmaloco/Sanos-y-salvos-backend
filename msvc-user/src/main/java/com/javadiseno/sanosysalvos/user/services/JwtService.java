package com.javadiseno.sanosysalvos.user.services;

import com.javadiseno.sanosysalvos.user.models.User;

/**
 * SY-4 Contrato para generación y validación del JWT.
 */
public interface JwtService {

    String generateToken(User user);

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
