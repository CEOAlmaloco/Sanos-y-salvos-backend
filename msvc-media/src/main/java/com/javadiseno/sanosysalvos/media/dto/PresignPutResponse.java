package com.javadiseno.sanosysalvos.media.dto;

public record PresignPutResponse(
        String bucket,
        String objectKey,
        String uploadUrl,
        int expiresInSeconds
) {}
