package com.javadiseno.sanosysalvos.matching.repositories;

import com.javadiseno.sanosysalvos.matching.models.ReportIndexItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportIndexRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${matching.dynamodb.table-name}")
    private String tableName;

    public void save(ReportIndexItem item) {
        table().putItem(item);
    }

    public List<ReportIndexItem> findByGeoHash(String geohash) {
        return query("GEO#" + geohash);
    }

    public List<ReportIndexItem> findByPetId(String petId) {
        return query("PET#" + petId);
    }

    public boolean matchAlreadyPublished(String reportIdA, String reportIdB) {
        String pairKey = pairKey(reportIdA, reportIdB);
        ReportIndexItem item = table().getItem(Key.builder()
                .partitionValue("MATCH#" + pairKey)
                .sortValue("META")
                .build());
        return item != null;
    }

    public void markMatchPublished(String reportIdA, String reportIdB) {
        table().putItem(ReportIndexItem.builder()
                .pk("MATCH#" + pairKey(reportIdA, reportIdB))
                .sk("META")
                .reportId(reportIdA)
                .petId(reportIdB)
                .indexedAt(java.time.Instant.now().toString())
                .build());
    }

    private List<ReportIndexItem> query(String partitionKey) {
        List<ReportIndexItem> items = new ArrayList<>();
        table().query(QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(partitionKey)
                        .build()))
                .items()
                .forEach(items::add);
        return items;
    }

    private DynamoDbTable<ReportIndexItem> table() {
        return dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(ReportIndexItem.class));
    }

    private static String pairKey(String reportIdA, String reportIdB) {
        if (reportIdA.compareTo(reportIdB) <= 0) {
            return reportIdA + "#" + reportIdB;
        }
        return reportIdB + "#" + reportIdA;
    }
}
