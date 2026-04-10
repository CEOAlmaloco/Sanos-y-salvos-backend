package com.javadiseno.sanosysalvos.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SY-2 - Bean de PasswordEncoder.
 *
 * BCrypt con stregth 10 es lo recomendado al:
 *  - Ser lo suficientemente lento para resistir ataques de fuerza bruta.
 *  - Ser lo suficientemente rápido para no degradar el login.
 *
 * @author Christián Mesa
 */
@Configuration // Indicar a Spring que en esa clase se define la configuración de la aplicación.
public class PasswordEncoderConfig {

    @Bean // Instanciar PasswordEncoder para inyecciones.
    public PasswordEncoder getPasswordEncoder() {

        return new BCryptPasswordEncoder(10);
    }
}
