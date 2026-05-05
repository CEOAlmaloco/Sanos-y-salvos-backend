package com.javadiseno.sanosysalvos.media.config;

import java.net.URI;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3Config {
    //s3Client es el que se conecta a S3/MinIO
    @Bean
    public S3Client s3Client(S3StorageProperties p) {
        var builder = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(p.getAccessKey(), p.getSecretKey())))
                .region(Region.of(p.getRegion()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(p.isPathStyleAccess())
                        .build());
        if (p.getEndpoint() != null && !p.getEndpoint().isBlank()) {
            builder.endpointOverride(URI.create(p.getEndpoint()));
        }
        return builder.build();
    }
    //s3Presigner es el que genera las URLs firmadas (GET/PUT)
    @Bean
    public S3Presigner s3Presigner(S3StorageProperties p) {
        var builder = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(p.getAccessKey(), p.getSecretKey())))
                .region(Region.of(p.getRegion()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(p.isPathStyleAccess())
                        .build());
        String presignUri = (p.getPresignEndpoint() != null && !p.getPresignEndpoint().isBlank())
                ? p.getPresignEndpoint()
                : p.getEndpoint();
        if (presignUri != null && !presignUri.isBlank()) {
            builder.endpointOverride(URI.create(presignUri));
        }
        return builder.build();
    }
}
