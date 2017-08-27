package com.github.immortaleeb.dynamodbmigrations.resolve;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.github.immortaleeb.dynamodbmigrations.migration.MigrationInfo;
import com.github.immortaleeb.dynamodbmigrations.table.SchemaVersionConstants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class DynamoDBMigrationInfo implements MigrationInfo {

    private final Map<String, AttributeValue> item;

    private final String version;
    private final String description;
    private final String script;
    private final LocalDateTime appliedOn;
    private final Duration executionTime;

    private DynamoDBMigrationInfo(Map<String, AttributeValue> item) {
        this.item = item;
        this.version = this.getMandatoryColumn(SchemaVersionConstants.COLUMN_VERSION);
        this.description = this.getMandatoryColumn(SchemaVersionConstants.COLUMN_DESCRIPTION);
        this.script = this.getMandatoryColumn(SchemaVersionConstants.COLUMN_SCRIPT);
        this.appliedOn = this.getAppliedOnFromItem();
        this.executionTime = this.getExecutionTimeFromItem();
    }

    private String getMandatoryColumn(String columnName) {
        AttributeValue attributeValue = this.item.get(columnName);
        String value = attributeValue == null ? null : attributeValue.getS();
        if (value == null) {
            throw new IllegalArgumentException("Item is missing value for mandatory column '" + columnName + "'");
        }
        return value;
    }

    private LocalDateTime getAppliedOnFromItem() {
        String appliedOnString = this.getMandatoryColumn(SchemaVersionConstants.COLUMN_APPLIED_ON);
        return LocalDateTime.parse(appliedOnString, DateTimeFormatter.ISO_DATE_TIME);
    }

    private Duration getExecutionTimeFromItem() {
        String executionTimeString = this.getMandatoryColumn(SchemaVersionConstants.COLUMN_EXECUTION_TIME);
        return Duration.of(Long.valueOf(executionTimeString), ChronoUnit.MILLIS);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public LocalDateTime getAppliedOn() {
        return appliedOn;
    }

    @Override
    public Duration getExecutionTime() {
        return executionTime;
    }

    public static DynamoDBMigrationInfo of(Map<String, AttributeValue> item) {
        return new DynamoDBMigrationInfo(item);
    }

    @Override
    public String toString() {
        return "DynamoDBMigrationInfo{version='" + version + '\'' + ", description='" +
                description + '\'' + ", script='" + script + '\'' + ", appliedOn=" + appliedOn + ", executionTime=" +
                executionTime + '}';
    }
}
