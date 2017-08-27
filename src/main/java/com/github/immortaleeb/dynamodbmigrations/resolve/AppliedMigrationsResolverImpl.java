package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppliedMigrationsResolverImpl implements AppliedMigrationsResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppliedMigrationsResolverImpl.class);

    private final AmazonDynamoDB client;
    private final String schemVersionTableName;

    public AppliedMigrationsResolverImpl(AmazonDynamoDB client, String schemVersionTableName) {
        this.client = client;
        this.schemVersionTableName = schemVersionTableName;
    }

    @Override
    public List<MigrationInfo> resolve() {
        return this.scanSchemaVersionTable().stream().map(DynamoDBMigrationInfo::of).collect(Collectors.toList());
    }

    private List<Map<String, AttributeValue>> scanSchemaVersionTable() {
        List<Map<String, AttributeValue>> items = new ArrayList<>();
        ScanResult scanResult = this.scanSchemaVersionTable(null);
        items.addAll(scanResult.getItems());

        while (scanResult.getLastEvaluatedKey() != null) {
            ScanResult result = this.scanSchemaVersionTable(scanResult.getLastEvaluatedKey());
            items.addAll(result.getItems());
        }

        return items;
    }

    private ScanResult scanSchemaVersionTable(Map<String, AttributeValue> exclusiveStartKey) {
        try {
            ScanRequest scanRequest = new ScanRequest(this.schemVersionTableName).withExclusiveStartKey(
                    exclusiveStartKey);
            ScanResult result = this.client.scan(scanRequest);

            Integer count = result.getCount();
            LOGGER.debug("Found {} items while scanning schema version table '{}'", count, this.schemVersionTableName);

            return result;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while scanning schema version table '" + this.schemVersionTableName + "'",
                    e);
        }
    }

}
