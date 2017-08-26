package com.github.immortaleeb.dynamodbmigrations.config;

public class Configuration {

    private String dynamoDBAccessKey;
    private String dynamoDBSecretKey;
    private String dynamoDBRegion;
    private String dynamoDBEndpointUrl;

    private String dynamoDBSchemaVersionTableName;
    private long dynamoDBSchemaVersionTableReadCapacity;
    private long dynamoDBSchemaVersionTableWriteCapacity;

    private Configuration(Builder builder) {
        dynamoDBAccessKey = builder.dynamoDBAccessKey;
        dynamoDBSecretKey = builder.dynamoDBSecretKey;
        dynamoDBRegion = builder.dynamoDBRegion;
        dynamoDBEndpointUrl = builder.dynamoDBEndpointUrl;
        dynamoDBSchemaVersionTableName = builder.dynamoDBSchemaVersionTableName;
        dynamoDBSchemaVersionTableReadCapacity = builder.dynamoDBSchemaVersionTableReadCapacity;
        dynamoDBSchemaVersionTableWriteCapacity = builder.dynamoDBSchemaVersionTableWriteCapacity;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getDynamoDBAccessKey() {
        return dynamoDBAccessKey;
    }

    public String getDynamoDBSecretKey() {
        return dynamoDBSecretKey;
    }

    public String getDynamoDBRegion() {
        return dynamoDBRegion;
    }

    public String getDynamoDBEndpointUrl() {
        return dynamoDBEndpointUrl;
    }

    public String getDynamoDBSchemaVersionTableName() {
        return dynamoDBSchemaVersionTableName;
    }

    public long getDynamoDBSchemaVersionTableReadCapacity() {
        return dynamoDBSchemaVersionTableReadCapacity;
    }

    public long getDynamoDBSchemaVersionTableWriteCapacity() {
        return dynamoDBSchemaVersionTableWriteCapacity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String dynamoDBAccessKey;
        private String dynamoDBSecretKey;
        private String dynamoDBRegion;
        private String dynamoDBEndpointUrl;
        private String dynamoDBSchemaVersionTableName;
        private long dynamoDBSchemaVersionTableReadCapacity;
        private long dynamoDBSchemaVersionTableWriteCapacity;

        private Builder() {
        }

        public Builder withDynamoDBAccessKey(String dynamoDBAccessKey) {
            this.dynamoDBAccessKey = dynamoDBAccessKey;
            return this;
        }

        public Builder withDynamoDBSecretKey(String dynamoDBSecretKey) {
            this.dynamoDBSecretKey = dynamoDBSecretKey;
            return this;
        }

        public Builder withDynamoDBRegion(String dynamoDBRegion) {
            this.dynamoDBRegion = dynamoDBRegion;
            return this;
        }

        public Builder withDynamoDBEndpointUrl(String dynamoDBEndpointUrl) {
            this.dynamoDBEndpointUrl = dynamoDBEndpointUrl;
            return this;
        }

        public Builder withDynamoDBSchemaVersionTableName(String dynamoDBSchemaVersionTableName) {
            this.dynamoDBSchemaVersionTableName = dynamoDBSchemaVersionTableName;
            return this;
        }

        public Builder withDynamoDBSchemaVersionTableReadCapacity(long dynamoDBSchemaVersionTableReadCapacity) {
            this.dynamoDBSchemaVersionTableReadCapacity = dynamoDBSchemaVersionTableReadCapacity;
            return this;
        }

        public Builder withDynamoDBSchemaVersionTableWriteCapacity(long dynamoDBSchemaVersionTableWriteCapacity) {
            this.dynamoDBSchemaVersionTableWriteCapacity = dynamoDBSchemaVersionTableWriteCapacity;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }

    }

}
