package com.javadiseno.sanosysalvos.integration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SY-40 Activa @CreatedDate en entidades
 */

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
