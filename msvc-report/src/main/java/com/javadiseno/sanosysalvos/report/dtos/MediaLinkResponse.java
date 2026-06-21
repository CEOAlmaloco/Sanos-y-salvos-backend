package com.javadiseno.sanosysalvos.report.dtos;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class MediaLinkResponse {

    private UUID id;
    private UUID reportId;
    private String url;
    private Integer sortOrder;
    private Instant createdAt;
}
