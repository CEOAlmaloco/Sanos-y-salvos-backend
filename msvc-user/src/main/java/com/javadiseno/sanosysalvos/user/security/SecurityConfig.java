package com.javadiseno.sanosysalvos.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF porque Postman no envía el token CSRF por defecto
                .csrf(csrf -> csrf.disable())

                // 2. Permitimos frames para que la consola de H2 se vea (usa frames)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // 3. Autorizamos acceso libre a todo por ahora
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Consola H2 libre
                        .anyRequest().permitAll()                      // Endpoints de registro libres
                );

        return http.build();
    }
}