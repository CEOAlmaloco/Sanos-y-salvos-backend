package com.javadiseno.sanosysalvos.media.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javadiseno.sanosysalvos.media.dtos.MediaUploadResponse;
import com.javadiseno.sanosysalvos.media.dtos.PresignPutRequest;
import com.javadiseno.sanosysalvos.media.dtos.PresignPutResponse;
import com.javadiseno.sanosysalvos.media.services.MediaStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaStorageService mediaStorageService;

    public MediaController(MediaStorageService mediaStorageService) {
        this.mediaStorageService = mediaStorageService;
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    /**
     * subida directa al bucket (MinIO o en despliegue S3)
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MediaUploadResponse upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false) String prefix
    ) throws IOException {
        return mediaStorageService.upload(file, prefix);
    }

    /**
     * Devuelve URL PUT prefirmada para que el cliente suba el archivo directo a MinIO.
     */
    @PostMapping("/presign-put")
    @ResponseStatus(HttpStatus.OK)
    public PresignPutResponse presignPut(@Valid @RequestBody PresignPutRequest body) {
        return mediaStorageService.presignPut(body);
    }

    /**
     * URL GET prefirmada para leer un objeto ya subido (sirve para los demas microservicios).
     */
    @GetMapping("/read-url")
    public ReadUrlResponse readUrl(
            @RequestParam("objectKey") String objectKey,
            @RequestParam(value = "seconds", defaultValue = "3600") int seconds
    ) {
        String decoded = URLDecoder.decode(objectKey, StandardCharsets.UTF_8);
        String url = mediaStorageService.presignGetUrl(decoded, seconds);
        return new ReadUrlResponse(decoded, url, seconds);
    }
    
    public record ReadUrlResponse(String objectKey, String readUrl, int expiresInSeconds) {}
}
