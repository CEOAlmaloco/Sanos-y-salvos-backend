package com.javadiseno.sanosysalvos.media.service;

import java.io.IOException; 
import java.time.Duration; // validez de las URLs firmadas (GET/PUT)
import java.util.UUID; 

import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Service; 
import org.springframework.web.multipart.MultipartFile; // fichero subido en multipart
import org.springframework.web.server.ResponseStatusException; 

import com.javadiseno.sanosysalvos.media.config.S3StorageProperties; // endpoint, bucket, credenciales
import com.javadiseno.sanosysalvos.media.dto.MediaUploadResponse; // JSON devuelto tras subir
import com.javadiseno.sanosysalvos.media.dto.PresignPutRequest; // body del presign PUT
import com.javadiseno.sanosysalvos.media.dto.PresignPutResponse; // JSON con URL PUT firmada

import jakarta.annotation.PostConstruct; // crear bucket al arrancar si falta
import software.amazon.awssdk.core.sync.RequestBody; // cuerpo del PutObject a S3
import software.amazon.awssdk.services.s3.S3Client; // cliente API S3/MinIO
import software.amazon.awssdk.services.s3.model.CreateBucketRequest; // crear bucket
import software.amazon.awssdk.services.s3.model.HeadBucketRequest; // comprobar si el bucket existe
import software.amazon.awssdk.services.s3.model.NoSuchBucketException; // bucket inexistente
import software.amazon.awssdk.services.s3.model.PutObjectRequest; // metadatos al subir un objeto
import software.amazon.awssdk.services.s3.model.S3Exception; // errores del servicio S3
import software.amazon.awssdk.services.s3.presigner.S3Presigner; // generar URLs firmadas
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest; // firma de lectura (GET)
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest; // firma de escritura (PUT)

@Service
public class MediaStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3StorageProperties properties;
    private final int presignPutSeconds;
    private final int presignGetSeconds;

    public MediaStorageService(
            S3Client s3Client,
            S3Presigner s3Presigner,
            S3StorageProperties properties
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.properties = properties;
        this.presignPutSeconds = 900; // 15 minutos
        this.presignGetSeconds = 3600; // 1 hora
    }

    @PostConstruct //se ejecuta al arrancar el servicio
    void ensureBucket() {
        String bucket = properties.getBucket();
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            if (e.statusCode() == 404) { //si el bucket no existe, se crea
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            } else {
                throw e;
            }
        }
    }

    public MediaUploadResponse upload(MultipartFile file, String prefix) throws IOException { //sube el archivo al bucket
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Archivo vacío o no recibido: use el campo form-data 'file' con un solo archivo por petición.");
        }
        String safeName = file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()
                ? file.getOriginalFilename()
                : "file";
        safeName = safeName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String objectKey = buildObjectKey(prefix, safeName); //se construye la clave del objeto en el bucket
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream"; 

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(properties.getBucket())
                        .key(objectKey)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        String readUrl = presignGetUrl(objectKey, presignGetSeconds); //se genera la URL GET prefirmada
        return new MediaUploadResponse(
                properties.getBucket(),
                objectKey,
                contentType,
                file.getSize(),
                readUrl
        );
    }

    public PresignPutResponse presignPut(PresignPutRequest req) {
        String safeName = req.fileName().replaceAll("[^a-zA-Z0-9._-]", "_");
        String objectKey = buildObjectKey(req.prefix(), safeName);
        String contentType = req.contentType() != null && !req.contentType().isBlank()
                ? req.contentType()
                : "application/octet-stream"; 

        var put = PutObjectRequest.builder() 
                .bucket(properties.getBucket())
                .key(objectKey)
                .contentType(contentType)
                .build();

        var presign = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofSeconds(presignPutSeconds))
                        .putObjectRequest(put)
                        .build()
        );

        return new PresignPutResponse(
                properties.getBucket(),
                objectKey,
                presign.url().toString(),
                presignPutSeconds
        );
    }

    /**
     * URL prefirmada GET (para guardar en report_media.url u otros servicios).
     */
    public String presignGetUrl(String objectKey, int seconds) {
        int s = Math.min(Math.max(seconds, 60), 86400);
        var get = software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(objectKey)
                .build();

        var presign = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofSeconds(s))
                        .getObjectRequest(get)
                        .build()
        );
        return presign.url().toString();
    }

    private String buildObjectKey(String prefix, String fileName) { //se construye la clave del objeto en el bucket
        String p = (prefix == null || prefix.isBlank()) ? "uploads" : prefix.trim().replaceAll("^/+|/+$", "");
        return p + "/" + UUID.randomUUID() + "_" + fileName; //se agrega un UUID al nombre del archivo 
    }
}
