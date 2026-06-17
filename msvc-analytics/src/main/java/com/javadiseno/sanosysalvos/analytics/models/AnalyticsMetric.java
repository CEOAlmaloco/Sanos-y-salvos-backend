package com.javadiseno.sanosysalvos.analytics.models;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;


// SY-71 Entidad que representa una métrica guardada en DynamoDB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class AnalyticsMetric {

    private String pk;

    private String sk;

    private String eventType;

    private String reportId;

    private String petId;

    private String userId;

    private Double latitude;

    private Double longitude;

    private String eventDate;

    private String createdAt;

    private Long ttl;

    @DynamoDbPartitionKey
    public String getPk() { return pk; }

    @DynamoDbSortKey
    public String getSk() { return sk; }

    @DynamoDbAttribute("eventType")
    public String getEventType() { return eventType; }

    @DynamoDbAttribute("ttl")
    public Long getTtl() { return ttl; }
}
