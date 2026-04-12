package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.model.User;

/**
 * SY-4 Contrato para generación y validación del JWT.
 */
public interface JwtService {

    String generateToken(User user);

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
