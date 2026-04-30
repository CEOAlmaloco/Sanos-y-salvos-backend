package com.javadiseno.sanosysalvos.media.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * API interna / pruebas: sin JWT hasta gateway. CAMBIAR A authenticated() cuando se cambie a produccion.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * CSRF (Cross-Site Request Forgery)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
