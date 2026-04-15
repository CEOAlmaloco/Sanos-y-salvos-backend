package com.javadiseno.sanosysalvos.user.config;

import com.javadiseno.sanosysalvos.user.model.Role;
import com.javadiseno.sanosysalvos.user.model.User;
import com.javadiseno.sanosysalvos.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TEMPORAL - ETAPA DEV:
 * Creación de usuario Admin al iniciar la aplicación
 */

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {
            if (userRepository.findUserByEmail("admin@test.com").isEmpty()){
                User admin = User.builder()
                        .id(UUID.randomUUID())
                        .email("admin@test.com")
                        .hashPassword(passwordEncoder.encode("admin123"))
                        .name("Admin")
                        .lastName("Salvos")
                        .phone("+56912345678")
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
                log.info("TEMPORAL - Usuario con rol {} y email {} creado y persistido con exito en H2", admin.getRole() ,admin.getEmail());
            }
        };
    }
}
