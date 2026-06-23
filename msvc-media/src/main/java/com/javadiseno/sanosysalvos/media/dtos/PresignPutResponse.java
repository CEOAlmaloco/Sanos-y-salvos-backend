package com.javadiseno.sanosysalvos.media.dtos;

public record PresignPutResponse(
        String bucket,
        String objectKey,
        String uploadUrl,
        int expiresInSeconds
) {}
