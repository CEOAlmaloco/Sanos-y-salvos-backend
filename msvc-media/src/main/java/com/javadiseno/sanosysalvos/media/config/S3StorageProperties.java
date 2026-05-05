package com.javadiseno.sanosysalvos.media.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sanos.s3")
public class S3StorageProperties {

    /**
     * Vacio = AWS S3 real. pero para MinIO en Docker: http://minio:9000 tmb http://localhost:9000
     */
    private String endpoint = "";

    private String region = "us-east-1";
    private String accessKey = "";
    private String secretKey = "";
    private String bucket = "sanos-media";
    private boolean pathStyleAccess = true;

    /**
     * Host usado solo al generar URLs firmadas (GET/PUT). Si esta vacio, se usa el {@link #endpoint}
     */
    private String presignEndpoint = "";

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public boolean isPathStyleAccess() {
        return pathStyleAccess;
    }

    public void setPathStyleAccess(boolean pathStyleAccess) {
        this.pathStyleAccess = pathStyleAccess;
    }

    public String getPresignEndpoint() {
        return presignEndpoint;
    }

    public void setPresignEndpoint(String presignEndpoint) {
        this.presignEndpoint = presignEndpoint;
    }
    //todos y cada uno de los getters y setters sirven para el S3
}
