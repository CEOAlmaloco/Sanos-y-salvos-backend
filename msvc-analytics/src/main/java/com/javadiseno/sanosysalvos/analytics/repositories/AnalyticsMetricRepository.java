package com.javadiseno.sanosysalvos.analytics.repositories;

import com.javadiseno.sanosysalvos.analytics.models.AnalyticsMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;

// SY-71 Repositorio para operaciones sobre DynamoDB
@Slf4j
@Repository
@RequiredArgsConstructor
public class AnalyticsMetricRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient ;

    @Value("${analytics.dynamodb.table-name}")
    private String tableName;

    private DynamoDbTable<AnalyticsMetric> table() {
        return dynamoDbEnhancedClient.table(tableName,
                TableSchema.fromBean(AnalyticsMetric.class));
    }

    public void save(AnalyticsMetric metric){
        try {
            table().putItem(metric);
            log.info("Métrica guardada: pk={} sk={}", metric.getPk(), metric.getSk());
        } catch (Exception e){
            log.error("Error al guardar métrica en DynamoDB: {}", e.getMessage());
            throw e;
        }
    }
}
