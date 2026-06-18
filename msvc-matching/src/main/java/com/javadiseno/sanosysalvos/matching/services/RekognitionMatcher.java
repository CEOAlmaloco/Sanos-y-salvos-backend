package com.javadiseno.sanosysalvos.matching.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RekognitionMatcher {

    private final RekognitionClient rekognitionClient;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${matching.rekognition.threshold:80}")
    private float rekognitionThreshold;

    public Optional<Float> compareFaces(String sourceKey, String targetKey) {
        if (sourceKey == null || sourceKey.isBlank() || targetKey == null || targetKey.isBlank()) {
            return Optional.empty();
        }

        try {
            CompareFacesRequest request = CompareFacesRequest.builder()
                    .sourceImage(Image.builder()
                            .s3Object(S3Object.builder().bucket(bucketName).name(sourceKey).build())
                            .build())
                    .targetImage(Image.builder()
                            .s3Object(S3Object.builder().bucket(bucketName).name(targetKey).build())
                            .build())
                    .similarityThreshold(rekognitionThreshold)
                    .build();

            var response = rekognitionClient.compareFaces(request);
            return response.faceMatches().stream()
                    .map(CompareFacesMatch::similarity)
                    .max(Float::compareTo);

        } catch (Exception e) {
            log.warn("Rekognition no disponible para {} vs {}: {}", sourceKey, targetKey, e.getMessage());
            return Optional.empty();
        }
    }
}
