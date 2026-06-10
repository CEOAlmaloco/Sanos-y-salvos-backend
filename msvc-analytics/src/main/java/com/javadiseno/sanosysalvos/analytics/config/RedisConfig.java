package com.javadiseno.sanosysalvos.analytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * SY-72 Configuración del cliente Redis para caché de zonas calientes.
 *
 * En el laboratorio AWS apuntar a ElastiCache
 */
@Configuration
public class RedisConfig {

    @Value("${analytics.redis.host:localhost}")
    private String redisHost;

    @Value("${analytics.redis.port:6379}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory factory){
        return new StringRedisTemplate(factory);
    }
}
