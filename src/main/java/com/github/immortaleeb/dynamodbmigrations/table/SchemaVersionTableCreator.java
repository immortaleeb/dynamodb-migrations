package com.github.immortaleeb.dynamodbmigrations.table;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionConstants.COLUMN_VERSION;

public class SchemaVersionTableCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaVersionTableCreator.class);

    private final AmazonDynamoDB client;

    public SchemaVersionTableCreator(AmazonDynamoDB client) {
        this.client = client;
    }

    public void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits) {
        KeySchemaElement versionKeySchemaElement = new KeySchemaElement()
                .withAttributeName(COLUMN_VERSION)
                .withKeyType(KeyType.HASH);

        AttributeDefinition versionAttributeDefinition = new AttributeDefinition()
                .withAttributeName(COLUMN_VERSION)
                .withAttributeType(ScalarAttributeType.S);

        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                .withKeySchema(versionKeySchemaElement)
                .withAttributeDefinitions(versionAttributeDefinition)
                .withProvisionedThroughput(new ProvisionedThroughput()
                    .withReadCapacityUnits(readCapacityUnits)
                    .withWriteCapacityUnits(writeCapacityUnits));

        try {
            LOGGER.debug("Creating schema version table '{}' if it does not already exist", tableName);

            boolean created = TableUtils.createTableIfNotExists(this.client, createTableRequest);

            if (created) {
                LOGGER.debug("Successfully created schema version table '{}‘", tableName);
            } else {
                LOGGER.debug("Skipped creation of schema version table '{}' because it already exists", tableName);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while trying to create schema version table '" + tableName + "'", e);
        }
    }

}
