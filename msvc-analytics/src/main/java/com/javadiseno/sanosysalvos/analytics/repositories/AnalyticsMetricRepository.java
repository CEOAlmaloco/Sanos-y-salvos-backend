package com.javadiseno.sanosysalvos.analytics.repositories;

import com.javadiseno.sanosysalvos.analytics.models.AnalyticsMetric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.stream.Collectors;

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

    // SY-71 Guarda una métrica en DynamoDB.
    public void save(AnalyticsMetric metric){
        try {
            table().putItem(metric);
            log.info("Métrica guardada: pk={} sk={}", metric.getPk(), metric.getSk());
        } catch (Exception e){
            log.error("Error al guardar métrica en DynamoDB: {}", e.getMessage());
            throw e;
        }
    }

    // SY-72 Consulta todas las métricas de un tipo de evento.
    public List<AnalyticsMetric> findByEventType(String eventType) {
        String pk = "EVENTO#" + eventType;

        QueryConditional condition = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(pk).build());

        return table().query(QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .build())
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    // SY-72 Consulta métricas de un tipo en un rango de fechas.
    public List<AnalyticsMetric> findByEventTypeAndDateRange(String eventType, String fromDate, String toDate) {

        String pk = "EVENTO#" + eventType;
        String skFrom = "FECHA#" + fromDate;
        String skTo = "FECHA#" + toDate + "#~";   // ~ es mayor que cualquier UUID en ASCII

        QueryConditional condition = QueryConditional.sortBetween(
                Key.builder().partitionValue(pk).sortValue(skFrom).build(),
                Key.builder().partitionValue(pk).sortValue(skTo).build()
        );

        return table().query(QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .build())
                .items()
                .stream()
                .collect(Collectors.toList());
    }
}
