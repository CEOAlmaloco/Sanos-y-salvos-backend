package com.javadiseno.sanosysalvos.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SY-1 Activa JPA Auditing
 *
 * Sin esta anotacion, los campos 'created_at' y 'updated_at' de la tabla 'user'
 * quedan null al persistir.
 *
 * @author Christián Mesa
 */
@Configuration // Indicar a Spring que en esa clase se define la configuración de la apliación.
@EnableJpaAuditing // Permite activar la auditoría de entidades en segundo plano.
public class JpaAuditingConfig {
    // No hay codigo, la anotación se encarga de proporcionar lo necesario para realizar el trabajo.
}
