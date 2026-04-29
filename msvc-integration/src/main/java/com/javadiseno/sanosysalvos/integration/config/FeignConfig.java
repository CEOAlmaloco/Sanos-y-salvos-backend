package com.javadiseno.sanosysalvos.integration.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.javadiseno.sanosysalvos.integration.client")
public class FeignConfig {
}
